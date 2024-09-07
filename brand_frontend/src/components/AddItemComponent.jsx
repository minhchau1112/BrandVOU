import React, {useState, useRef, useEffect} from 'react';
import { Form, Button, Container, Image, Alert, Row, Col } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import Select from 'react-select';
import {useAuth} from "../AuthProvider";
import eventService from "../services/EventService";
import ItemService from "../services/ItemService";

function AddItemComponent() {
    const [item, setItem] = useState({
        name: '',
        image: null,
        type: "Coin",
    });
    const [events, setEvents] = useState([]);
    const [selectedEvent, setSelectedEvent] = useState(null);

    const [previewImage, setPreviewImage] = useState(null);
    const [error, setError] = useState(null);
    const [message, setMessage] = useState(null);
    const navigate = useNavigate();


    const imageInputRef = useRef(null);
    const auth = useAuth();

    useEffect(() => {
        const fetchEventsHaveTargetWord = async () => {
            try {
                const brandID = auth.brand.id;

                const response = await eventService.getEventsOfBrandHaveTargetWord(brandID);
                console.log("events:", response.data);

                const eventOptions = response.data.map(event => ({
                    value: event.id,
                    label: event.name
                }));
                setEvents(eventOptions);
            } catch (error) {
                setError('Error fetching events.');
            }
        };

        fetchEventsHaveTargetWord();
    }, [auth.brand.id]);

    const handleEventChange = (selectedOption) => {
        setSelectedEvent(selectedOption ? selectedOption.value : null);
    };

    const handleChange = (e) => {
        const { name, value, type, files } = e.target;
        if (type === 'file') {
            const file = files[0];
            setItem({ ...item, [name]: file });
            const reader = new FileReader();
            reader.onloadend = () => {
                setPreviewImage(reader.result);
            };
            if (file) {
                reader.readAsDataURL(file);
            }
        } else {
            setItem({ ...item, [name]: value });
        }
    };

    const handleBack = () => {
        navigate(-1);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!item.image) {
            setError('Please add Image before submitting.');
            return;
        }

        console.log('Item data before submit:', item);

        const formData = new FormData();
        formData.append('eventId', selectedEvent);
        formData.append('name', item.name.toString());
        formData.append('type', item.type);
        formData.append('image', item.image);

        try {
            let id = await ItemService.createItem(formData);

            if (id.data === -1) {
                setError('Item already exist! There is another item with given name')
                return;
            }

            setMessage("Item created successfully!");
            navigate(`/items/view-detail/${id.data}`);
        } catch (err) {
            setError('There was an error creating the item. Please try again.');
        }
    }

    return (
        <Container className='mt-5'>
            <h2 className="text-center">Add Item For Event</h2>
            {error && <Alert variant="danger">{error}</Alert>}
            {message && <div className="alert alert-success">{message}</div>}
            <Row className='mt-5'>
                <Col md={6}>
                    <Form onSubmit={handleSubmit}>
                        <Form.Group controlId="formItemName">
                            <Form.Label>Item Name</Form.Label>
                            <Form.Control
                                type="number"
                                name="name"
                                value={item.name}
                                onChange={handleChange}
                                placeholder="Enter item name"
                                required
                            />
                        </Form.Group>


                        <Form.Group controlId="formGameType" className="mt-1">
                            <Form.Label>Event</Form.Label>

                            <Select
                                name="events"
                                options={events}
                                className="basic-single"
                                classNamePrefix="select"
                                onChange={handleEventChange}
                                required
                            />
                        </Form.Group>

                        <Form.Group controlId="formType" className="mt-1">
                            <Form.Label>Type</Form.Label>
                            <Form.Control
                                type="text"
                                name="targetWord"
                                value={item.type}
                                disabled
                            />
                        </Form.Group>

                        <div className="d-flex justify-content-start mt-4" style={{gap: '12px'}}>
                            <Button variant="secondary" onClick={handleBack}>
                                <i className="bi bi-arrow-left mr-2"></i> Back
                            </Button>

                            <Button variant="primary" type="submit">
                                Save Item
                            </Button>
                        </div>

                    </Form>
                </Col>
                <Col md={6}>
                    <div
                        style={{ width: '100%', height: '320px', backgroundColor: '#F0F0F0', cursor: 'pointer', position: 'relative', overflow: 'hidden' }}
                        className="image-container mb-3"
                        onClick={() => imageInputRef.current.click()}
                    >
                        {previewImage && (
                            <Image
                                src={previewImage}
                                alt="Event Image Preview"
                                style={{
                                    width: '100%',
                                    height: '100%',
                                    objectFit: 'contain'
                                }}
                            />
                        )}
                        <div className="overlay">
                            <div className="overlay-text">Add Image +</div>
                        </div>
                    </div>
                    <Form.Group controlId="formFile" className="d-none">
                        <Form.Control
                            type="file"
                            name="image"
                            onChange={handleChange}
                            ref={imageInputRef}
                            required
                        />
                    </Form.Group>
                </Col>
            </Row>

        </Container>
    );
}

export default AddItemComponent;