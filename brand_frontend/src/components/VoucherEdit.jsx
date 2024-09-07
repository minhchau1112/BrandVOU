import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Form, Button, Container, Row, Col, Spinner, Modal, Image } from 'react-bootstrap';
import VoucherService from '../services/VoucherService';
import EventService from '../services/EventService';
import Select from 'react-select';
import './AddVoucherComponent.css';
import { useAuth } from "../AuthProvider";

function VoucherEdit() {
    const { id } = useParams();
    const navigate = useNavigate();
    const [voucher, setVoucher] = useState({
        code: '',
        qrcode: null,
        image: null,
        value: '',
        description: '',
        expirationDate: '',
        status: '',
        count: 0
    });
    const [previewImage, setPreviewImage] = useState(null);
    const [previewQrCode, setPreviewQrCode] = useState(null);
    const [events, setEvents] = useState([]);
    const [selectedEvent, setSelectedEvent] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [message, setMessage] = useState(null);
    const [showDeleteModal, setShowDeleteModal] = useState(false);

    const auth = useAuth();

    useEffect(() => {
        const fetchVoucherDetails = async () => {
            try {
                const response = await VoucherService.getVoucherByVoucherId(id);
                setVoucher(response.data);
                setPreviewImage(response.data.image);
                setPreviewQrCode(response.data.qrcode);
                setSelectedEvent(response.data.event.id);
                setLoading(false);

                const eventResponse = await EventService.getAllEventsByBrandId(auth.brand.id);
                const eventOptions = eventResponse.data.map(event => ({
                    value: event.id,
                    label: event.name
                }));
                setEvents(eventOptions);
            } catch (error) {
                // setError('Error fetching voucher details or events.');
                setLoading(false);
            }
        };

        fetchVoucherDetails();
    }, [id, auth.brand.id]);

    const handleChange = (e) => {
        const { name, value, type, files } = e.target;
        if (type === 'file') {
            const file = files[0];
            setVoucher({ ...voucher, [name]: file });
            const reader = new FileReader();
            reader.onloadend = () => {
                if (name === 'image') {
                    setPreviewImage(reader.result);
                } else if (name === 'qrCode') {
                    setPreviewQrCode(reader.result);
                }
            };
            if (file) {
                reader.readAsDataURL(file);
            }
        } else {
            setVoucher({ ...voucher, [name]: value });
        }
    };

    const handleEventChange = (selectedOption) => {
        setSelectedEvent(selectedOption ? selectedOption.value : null);
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

    const handleSubmit = async (e) => {
        e.preventDefault();

        console.log("voucher: ", voucher);

        const formData = new FormData();
    
        formData.append('code', voucher.code);
        formData.append('value', voucher.value);
        formData.append('description', voucher.description);
        formData.append('expirationDate', voucher.expirationDate);
        formData.append('status', voucher.status);
        formData.append('eventId', selectedEvent);
        formData.append('count', voucher.count);

        if (typeof voucher.qrcode === 'string') {
            const file = await createFileFromUrl(voucher.qrcode);
            formData.append('QRCode', file);
        } else {
            formData.append('QRCode', voucher.qrcode);
        }

        if (typeof voucher.image === 'string') {
            const file = await createFileFromUrl(voucher.image);
            formData.append('image', file);
        } else {
            formData.append('image', voucher.image);
        }

        for (let [key, value] of formData.entries()) {
            console.log(key, value);
        }

        try {
            await VoucherService.updateVoucher(formData, id);
            setMessage('Voucher updated successfully!');
            navigate(`/vouchers/view-detail/${id}`);
        } catch (err) {
            setError('Error updating voucher. Please try again.');
        }
    };

    const handleDelete = async () => {
        try {
            await VoucherService.deleteVoucher(id);
            setShowDeleteModal(false);
            navigate('/vouchers'); // Redirect to vouchers list after deletion
        } catch (err) {
            setError('Error deleting voucher. Please try again.');
        }
    };

    if (loading) {
        return <Spinner animation="border" />;
    }

    return (
        <Container>
            {error && <div className="alert alert-danger">{error}</div>}
            {message && <div className="alert alert-success">{message}</div>}
            <Row className="mt-5">
                <Col md={6} className="d-flex flex-column align-items-left" style={{ paddingTop: '50px' }}>
                    <h2>Edit Voucher</h2>
                    <p>Update the details for the voucher</p>
                    <Form.Group controlId="formDescription">
                        <Form.Label className='label'>Description</Form.Label>
                        <Form.Control 
                            type="text" 
                            name="description" 
                            value={voucher.description} 
                            onChange={handleChange} 
                            placeholder="Enter voucher description" 
                        />
                    </Form.Group>
                </Col>
                <Col md={6} className="d-flex flex-column align-items-center">
                    <div
                        style={{ width: '100%', height: '300px', backgroundColor: '#F0F0F0', cursor: 'pointer', position: 'relative', overflow: 'hidden' }}
                        className="image-container mb-3"
                        onClick={() => document.getElementById('qrCodeInput').click()}
                    >
                        {previewQrCode && (
                            <Image 
                                src={previewQrCode} 
                                alt="Voucher QR Code Preview" 
                                style={{ 
                                    width: '100%', 
                                    height: '100%', 
                                    objectFit: 'contain' 
                                }} 
                            />
                        )}
                        <div className="overlay">
                            <div className="overlay-text">Edit QR Code +</div>
                        </div>
                    </div>
                    <Form.Group controlId="qrCodeInput" className="d-none">
                        <Form.Control
                            type="file"
                            name="qrCode"
                            onChange={handleChange}
                        />
                    </Form.Group>
                </Col>
            </Row>
            <Row className="mt-5">
                <Col md={6}>
                    <div
                        style={{ width: '100%', height: '320px', backgroundColor: '#F0F0F0', cursor: 'pointer', position: 'relative', overflow: 'hidden' }}
                        className="image-container mb-3"
                        onClick={() => document.getElementById('imageInput').click()}
                    >
                        {previewImage && (
                            <Image 
                                src={previewImage} 
                                alt="Voucher Image Preview" 
                                style={{ 
                                    width: '100%', 
                                    height: '100%', 
                                    objectFit: 'contain' 
                                }} 
                            />
                        )}
                        <div className="overlay">
                            <div className="overlay-text">Edit Image +</div>
                        </div>
                    </div>
                    <Form.Group controlId="imageInput" className="d-none">
                        <Form.Control
                            type="file"
                            name="image"
                            onChange={handleChange}
                        />
                    </Form.Group>
                </Col>
                <Col md={6}>
                    <h3>Voucher Details</h3>
                    <Form onSubmit={handleSubmit}>
                        <Form.Group controlId="formEvent">
                            <Form.Label className='label'>Event</Form.Label>
                            <Select
                                name="event"
                                options={events}
                                className="basic-single"
                                classNamePrefix="select"
                                onChange={handleEventChange}
                                placeholder="Select an event"
                                value={events.find(event => event.value === selectedEvent)}
                            />
                        </Form.Group>
                        <div className='d-flex justify-content-between mt-1'>
                            <Form.Group as={Col} md={5} controlId="formCode">
                                <Form.Label className='label'>Code</Form.Label>
                                <Form.Control 
                                    type="text" 
                                    name="code" 
                                    value={voucher.code} 
                                    onChange={handleChange} 
                                    placeholder="Enter voucher code" 
                                    disabled 
                                />
                            </Form.Group>

                            <Form.Group as={Col} md={6} controlId="formValue">
                                <Form.Label className='label'>Value</Form.Label>
                                <Form.Control 
                                    type="number" 
                                    name="value" 
                                    value={voucher.value} 
                                    onChange={handleChange} 
                                    placeholder="Enter voucher value" 
                                    required 
                                />
                            </Form.Group>
                        </div>

                        <div className='d-flex justify-content-between mt-1'>
                            <Form.Group as={Col} md={5} controlId="formExpirationDate">
                                <Form.Label className='label'>Expiration Date</Form.Label>
                                <Form.Control 
                                    type="datetime-local" 
                                    name="expirationDate" 
                                    value={voucher.expirationDate} 
                                    onChange={handleChange} 
                                    required 
                                />
                            </Form.Group>

                            <Form.Group as={Col} md={3} controlId="formStatus">
                                <Form.Label className='label'>Status</Form.Label>
                                <Form.Control 
                                    as="select" 
                                    name="status" 
                                    value={voucher.status} 
                                    onChange={handleChange} 
                                    required
                                >
                                    <option value="Active">Active</option>
                                    <option value="Inactive">Inactive</option>
                                </Form.Control>
                            </Form.Group>

                            <Form.Group as={Col} md={3} controlId="formCount">
                                <Form.Label className='label'>Number of voucher</Form.Label>
                                <Form.Control 
                                    type="number" 
                                    name="count" 
                                    value={voucher.count} 
                                    onChange={handleChange} 
                                    required
                                >
                                </Form.Control>
                            </Form.Group>
                        </div>

                        <div className="d-flex justify-content-start mt-4" style={{gap: '12px'}}>
                            <Button variant="secondary" onClick={handleBack}>
                                <i className="bi bi-arrow-left mr-2"></i> Back
                            </Button>

                            <Button variant="primary" type="submit">
                                Save Changes
                            </Button>

                            <Button variant="danger" onClick={() => setShowDeleteModal(true)}>
                                Delete Voucher
                            </Button>
                        </div>
                    </Form>
                </Col>
            </Row>

            <Modal show={showDeleteModal} onHide={() => setShowDeleteModal(false)}>
                <Modal.Header closeButton>
                    <Modal.Title>Confirm Deletion</Modal.Title>
                </Modal.Header>
                <Modal.Body>Are you sure you want to delete this voucher?</Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={() => setShowDeleteModal(false)}>
                        Cancel
                    </Button>
                    <Button variant="danger" onClick={handleDelete}>
                        Delete
                    </Button>
                </Modal.Footer>
            </Modal>
        </Container>
    );
}

export default VoucherEdit;