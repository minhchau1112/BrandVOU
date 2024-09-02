import React, { useState, useRef } from 'react';
import { Form, Button, Container, Image, Alert, Row, Col } from 'react-bootstrap';
import EventService from '../services/EventService';
import { useNavigate } from 'react-router-dom';
import Select from 'react-select';
import {useAuth} from "../AuthProvider";

function AddEventComponent({brandID}) {
    const [event, setEvent] = useState({
        name: '',
        image: null,
        voucherCount: 0,
        startTime: '',
        endTime: '',
        gameType: ''
    });
    const [previewImage, setPreviewImage] = useState(null);
    const [error, setError] = useState(null);
    const [message, setMessage] = useState(null);
    const navigate = useNavigate();

    const gameOptions = [
        { value: 'Quiz', label: 'Quiz' },
        { value: 'ShakeGame', label: 'ShakeGame' }
    ];

    const imageInputRef = useRef(null);
    const auth = useAuth();
    brandID = auth.brand.id;
    const handleChange = (e) => {
        const { name, value, type, files } = e.target;
        if (type === 'file') {
            const file = files[0];
            setEvent({ ...event, [name]: file });
            const reader = new FileReader();
            reader.onloadend = () => {
                setPreviewImage(reader.result);
            };
            if (file) {
                reader.readAsDataURL(file);
            }
        } else {
            setEvent({ ...event, [name]: value });
        } 
    };

    const handleGameTypeChange = (selectedOptions) => {
        const gameTypes = selectedOptions.map(option => option.value).join(';');
        setEvent({ ...event, gameType: gameTypes });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

		// let isDuplicate = await EventService.checkDuplicate(event.name, brandID);
		// console.log("isDuplicate: ", isDuplicate.data);
		
		// if (isDuplicate.data) {
		// 	setError('Event Name already exists for this brand! Please enter another event name');
		// 	return;
		// }

        if (new Date(event.startTime) >= new Date(event.endTime)) {
            setError('End time must be after start time.');
            return;
        }

        if (!event.image) {
            setError('Please add Image before submitting.');
            return;
        }

        console.log('Event data before submit:', event);

        const formData = new FormData();
        formData.append('brandId', brandID);
        formData.append('name', event.name);
        formData.append('image', event.image);
        formData.append('voucherCount', event.voucherCount);
        formData.append('startTime', event.startTime);
        formData.append('endTime', event.endTime);
        formData.append('gameType', event.gameType); 

        try {
            await EventService.createEvent(formData);
            setMessage("Event created successfully!");
            navigate('/');
        } catch (err) {
            setError('There was an error creating the event. Please try again.');
        };
    }
    return (
        <Container className='mt-5'>
            <h2 className="text-center">Add New Event</h2>
            {error && <Alert variant="danger">{error}</Alert>}
			{message && <div className="alert alert-success">{message}</div>}
            <Row className='mt-5'>
                <Col md={6}>
                    <Form onSubmit={handleSubmit}>
                        <Form.Group controlId="formEventName">
                            <Form.Label>Event Name</Form.Label>
                            <Form.Control 
                                type="text" 
                                name="name" 
                                value={event.name} 
                                onChange={handleChange} 
                                placeholder="Enter event name" 
                                required 
                            />
                        </Form.Group>

                        <Form.Group controlId="formvoucherCount">
                            <Form.Label>Number of Vouchers</Form.Label>
                            <Form.Control 
                                type="number" 
                                name="voucherCount" 
                                value={event.voucherCount} 
                                onChange={handleChange} 
                                placeholder="Enter number of vouchers" 
                                required 
                            />
                        </Form.Group>

                        <div className='d-flex justify-content-between mt-1'>
                            <Form.Group as={Col} md={5} controlId="formStartTime">
                                <Form.Label>Start Time</Form.Label>
                                <Form.Control 
                                    type="datetime-local" 
                                    name="startTime" 
                                    value={event.startTime} 
                                    onChange={handleChange} 
                                    required 
                                />
                            </Form.Group>

                            <Form.Group as={Col} md={5} controlId="formEndTime">
                                <Form.Label>End Time</Form.Label>
                                <Form.Control 
                                    type="datetime-local" 
                                    name="endTime" 
                                    value={event.endTime} 
                                    onChange={handleChange} 
                                    required 
                                />
                            </Form.Group>
                        </div>
                        

                        <Form.Group controlId="formGameType">
                            <Form.Label>Game Type</Form.Label>
                            <Select
                                isMulti
                                name="gameType"
                                options={gameOptions}
                                className="basic-multi-select"
                                classNamePrefix="select"
                                onChange={handleGameTypeChange}
                                required
                            />
                        </Form.Group>
                        
                        <Button variant="primary" type="submit" className='mt-3'>
                            Submit
                        </Button>
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

export default AddEventComponent;