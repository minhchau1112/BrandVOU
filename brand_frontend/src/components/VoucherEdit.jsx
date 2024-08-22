import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Form, Button, Container, Alert, Spinner } from 'react-bootstrap';
import VoucherService from '../services/VoucherService';
import EventService from '../services/EventService';
import Select from 'react-select';

function VoucherEdit() {
    const { id } = useParams();
    const navigate = useNavigate();
    const [voucher, setVoucher] = useState({
        code: '',
        qrCode: null,
        image: null,
        value: '',
        description: '',
        expirationDate: '',
        status: ''
    });
    const [previewImage, setPreviewImage] = useState(null);
    const [previewQrCode, setPreviewQrCode] = useState(null);
    const [events, setEvents] = useState([]);
    const [selectedEvent, setSelectedEvent] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [message, setMessage] = useState(null);

    useEffect(() => {
		const fetchVoucherDetails = async () => {
			try {
				const response = await VoucherService.getVoucherByVoucherId(id);
				setVoucher(response.data);
				setPreviewImage(response.data.image);
				setPreviewQrCode(response.data.qrcode);
				setSelectedEvent(response.data.event.id);
				setLoading(false);
				console.log('response: ', response);
	
				const eventResponse = await EventService.getEventsByBrandId(response.data.event.brand.id);
				const eventOptions = eventResponse.data.map(event => ({
					value: event.id,
					label: event.name
				}));
				setEvents(eventOptions);
			} catch (error) {
				setError('Error fetching voucher details or events.');
				setLoading(false);
			}
		};
	
		fetchVoucherDetails();
	}, [id]);

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
        formData.append('eventId', selectedEvent);

        try {
            await VoucherService.updateVoucher(formData, id);
            setMessage('Voucher updated successfully!');
            navigate(`/vouchers/view-detail/${id}`);
        } catch (err) {
            setError('Error updating voucher. Please try again.');
        }
    };

    if (loading) {
        return <Spinner animation="border" />;
    }

    return (
        <Container className='mt-5'>
            <h2 className="text-center">Edit Voucher</h2>
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
                        value={events.find(event => event.value === selectedEvent)}
                    />
                </Form.Group>

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
                        type="file"
                        name="qrCode"
                        onChange={handleChange}
                    />
                    {previewQrCode && (
                        <div className="mt-3">
                            <img src={previewQrCode} alt="QR Code Preview" style={{ maxWidth: '200px' }} />
                        </div>
                    )}
                </Form.Group>

                <Form.Group controlId="formImage">
                    <Form.Label>Image</Form.Label>
                    <Form.Control
                        type="file"
                        name="image"
                        onChange={handleChange}
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

                <Button variant="primary" type="submit" className='mt-3'>
                    Submit
                </Button>
            </Form>
        </Container>
    );
}

export default VoucherEdit;