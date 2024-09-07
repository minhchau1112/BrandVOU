import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import {
    Form,
    Container,
    Button,
    Spinner,
    Alert,
    Image,
    Row,
    Col, Modal,
} from 'react-bootstrap';
import ItemService from "../services/ItemService";
import itemService from "../services/ItemService";

function ItemEdit() {
    const { id } = useParams();
    const navigate = useNavigate();
    const [item, setItem] = useState({
        name: '',
        image: null,
        event: '',
        type: ''
    });
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [previewImage, setPreviewImage] = useState(null);
    const [showDeleteModal, setShowDeleteModal] = useState(false);

    useEffect(() => {
        const fetchItemDetail = async () => {
            try {
                const response = await ItemService.getItemByItemId(id);
                console.log("item: ", response.data);
                setItem(response.data);
                setPreviewImage(response.data.image);
                setLoading(false);
            } catch (error) {
                setError('Error fetching item.');
                setLoading(false);
            }
        };

        fetchItemDetail();
    }, [id]);

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

    async function createFileFromUrl(imageUrl) {
        try {
            const response = await fetch(imageUrl);

            const blob = await response.blob();

            const imageName = imageUrl.split('/').pop() || 'image.jpg';

            const file = new File([blob], imageName, { type: blob.type });

            return file;
        } catch (error) {
            console.error('Error creating file from URL:', error);
        }
    }

    const handleDelete = async () => {
        try {
            await ItemService.deleteItem(id);
            setShowDeleteModal(false);
            navigate('/items'); // Redirect to vouchers list after deletion
        } catch (err) {
            setError('Error deleting item. Please try again.');
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const formData = new FormData();

            if (typeof item.image === 'string') {
                const file = await createFileFromUrl(item.image);
                formData.append('image', file);
            } else {
                formData.append('image', item.image);
            }

            console.log("form-data:", formData.image);

            await itemService.updateItem(id, formData);
            navigate(`/items/view-detail/${id}`);
        } catch (error) {
            setError('Error updating item.');
        }
    };


    if (loading) {
        return <Spinner animation="border" />;
    }

    if (error) {
        return <Alert variant="danger">{error}</Alert>;
    }

    return (
        <Container className="mt-5">
            <h2 className="text-center">Edit Event</h2>
            <Row className="mt-5">
                <Col md={6}>
                    <Form onSubmit={handleSubmit}>
                        <Form.Group controlId="formItemName">
                            <Form.Label>Item Name</Form.Label>
                            <Form.Control
                                type="text"
                                name="name"
                                value={item.name}
                                onChange={handleChange}
                                disabled
                            />
                        </Form.Group>

                        <Form.Group controlId="formEventName">
                            <Form.Label>Event Name</Form.Label>
                            <Form.Control
                                type="text"
                                name="eventName"
                                value={item.event.name}
                                onChange={handleChange}
                                disabled
                            />
                        </Form.Group>

                        <Form.Group controlId="formType">
                            <Form.Label>Type</Form.Label>
                            <Form.Control
                                type="text"
                                value={item?.type}
                                disabled
                            />
                        </Form.Group>

                        <div className="d-flex justify-content-start mt-4" style={{gap: '12px'}}>
                            <Button variant="secondary" onClick={handleBack}>
                                <i className="bi bi-arrow-left mr-2"></i> Back
                            </Button>

                            <Button variant="primary" type="submit">
                                Save Changes
                            </Button>

                            <Button variant="danger" onClick={() => setShowDeleteModal(true)}>
                                <i className="bi bi-trash3 mr-2"></i> Delete Item
                            </Button>
                        </div>
                    </Form>
                </Col>

                <Modal show={showDeleteModal} onHide={() => setShowDeleteModal(false)}>
                    <Modal.Header closeButton>
                        <Modal.Title>Confirm Deletion</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>Are you sure you want to delete this item?</Modal.Body>
                    <Modal.Footer>
                        <Button variant="secondary" onClick={() => setShowDeleteModal(false)}>
                            Cancel
                        </Button>
                        <Button variant="danger" onClick={handleDelete}>
                            Delete
                        </Button>
                    </Modal.Footer>
                </Modal>

                <Col md={6}>
                    <div
                        style={{ width: '100%', height: '320px', backgroundColor: '#F0F0F0', cursor: 'pointer', position: 'relative', overflow: 'hidden' }}
                        className="image-container mb-3"
                        onClick={() => document.querySelector('input[name="image"]').click()}
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
                            accept="image/*"
                        />
                    </Form.Group>
                </Col>
            </Row>

        </Container>
    );
}

export default ItemEdit;