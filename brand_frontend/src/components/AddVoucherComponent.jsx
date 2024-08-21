import React, { useState, useEffect } from 'react';
import { Form, Button, Container, Alert, Spinner } from 'react-bootstrap';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import Select from 'react-select';

function AddVoucherComponent({ brandID }) {
    const [voucher, setVoucher] = useState({
        code: '',
        qrCode: '',
        image: null,
        value: '',
        description: '',
        expirationDate: '',
        status: ''
    });
    const [previewImage, setPreviewImage] = useState(null);
    const [error, setError] = useState(null);
    const [message, setMessage] = useState(null);
    const [events, setEvents] = useState([]);
    const [selectedEvent, setSelectedEvent] = useState(null);
    const [eventDetails] = useState(null);
    const [loading] = useState(false);
    const navigate = useNavigate();

    // Lấy danh sách sự kiện của thương hiệu
    useEffect(() => {
        const fetchEvents = async () => {
            try {
                const response = await axios.get(`http://localhost:9090/api/v1/events/${brandID}`);
                const eventOptions = response.data.map(event => ({
                    value: event.id,
                    label: event.name
                }));
                setEvents(eventOptions);
            } catch (error) {
                setError('Error fetching events.');
            }
        };

        fetchEvents();
    }, [brandID]);

    const handleChange = (e) => {
        const { name, value, type, files } = e.target;
        if (type === 'file') {
            const file = files[0];
            setVoucher({ ...voucher, [name]: file });
            const reader = new FileReader();
            reader.onloadend = () => {
                setPreviewImage(reader.result);
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

    const handleSubmit = async (e) => {
        e.preventDefault();

        const formData = new FormData();
        formData.append('code', voucher.code);
        formData.append('qrCode', voucher.qrCode);
        formData.append('image', voucher.image);
        formData.append('value', voucher.value);
        formData.append('description', voucher.description);
        formData.append('expirationDate', voucher.expirationDate);
        formData.append('status', voucher.status);

        try {
            await axios.post(`http://localhost:9090/api/v1/vouchers/${selectedEvent}`, formData, {
                headers: {
                    'Content-Type': 'multipart/form-data'
                }
            });
            setMessage('Voucher added successfully!');
            navigate('/vouchers');
        } catch (err) {
            setError('Error adding voucher. Please try again.');
        }
    };

    return (
        <Container>
            <h2 className="text-center">Add New Voucher</h2>
            {error && <Alert variant="danger">{error}</Alert>}
            {message && <Alert variant="success">{message}</Alert>}
            <Form onSubmit={handleSubmit}>
                <Form.Group controlId="formEvent">
                    <Form.Label>Event</Form.Label>
                    <Select
                        name="event"
                        options={events}
                        className="basic-single"
                        classNamePrefix="select"
                        onChange={handleEventChange}
                        placeholder="Select an event"
                    />
                </Form.Group>
                {loading && <Spinner animation="border" />}
                {eventDetails && (
                    <div className="mt-3">
                        <h5>Event Details</h5>
                        <p><strong>Name:</strong> {eventDetails.name}</p>
                        <p><strong>Start Time:</strong> {eventDetails.startTime}</p>
                        <p><strong>End Time:</strong> {eventDetails.endTime}</p>
                        <p><strong>Number of Vouchers:</strong> {eventDetails.voucherCount}</p>
                        {/* Display more details if needed */}
                    </div>
                )}

                <Form.Group controlId="formCode">
                    <Form.Label>Code</Form.Label>
                    <Form.Control 
                        type="text" 
                        name="code" 
                        value={voucher.code} 
                        onChange={handleChange} 
                        placeholder="Enter voucher code" 
                        required 
                    />
                </Form.Group>

                <Form.Group controlId="formQrCode">
                    <Form.Label>QR Code</Form.Label>
                    <Form.Control 
                        type="text" 
                        name="qrCode" 
                        value={voucher.qrCode} 
                        onChange={handleChange} 
                        placeholder="Enter QR code" 
                        required 
                    />
                </Form.Group>

                <Form.Group controlId="formImage">
                    <Form.Label>Image</Form.Label>
                    <Form.Control 
                        type="file" 
                        name="image" 
                        onChange={handleChange} 
                        required 
                    />
                    {previewImage && (
                        <div className="mt-3">
                            <img src={previewImage} alt="Voucher Preview" style={{ maxWidth: '200px' }} />
                        </div>
                    )}
                </Form.Group>

                <Form.Group controlId="formValue">
                    <Form.Label>Value</Form.Label>
                    <Form.Control 
                        type="number" 
                        name="value" 
                        value={voucher.value} 
                        onChange={handleChange} 
                        placeholder="Enter voucher value" 
                        required 
                    />
                </Form.Group>

                <Form.Group controlId="formDescription">
                    <Form.Label>Description</Form.Label>
                    <Form.Control 
                        type="text" 
                        name="description" 
                        value={voucher.description} 
                        onChange={handleChange} 
                        placeholder="Enter voucher description" 
                        required 
                    />
                </Form.Group>

                <Form.Group controlId="formExpirationDate">
                    <Form.Label>Expiration Date</Form.Label>
                    <Form.Control 
                        type="datetime-local" 
                        name="expirationDate" 
                        value={voucher.expirationDate} 
                        onChange={handleChange} 
                        required 
                    />
                </Form.Group>

                <Form.Group controlId="formStatus">
                    <Form.Label>Status</Form.Label>
                    <Form.Control 
                        type="text" 
                        name="status" 
                        value={voucher.status} 
                        onChange={handleChange} 
                        placeholder="Enter voucher status" 
                        required 
                    />
                </Form.Group>

                <Button variant="primary" type="submit">
                    Submit
                </Button>
            </Form>
        </Container>
    );
}

export default AddVoucherComponent;