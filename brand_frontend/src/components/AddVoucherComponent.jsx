import React, { useState, useEffect, useRef } from 'react';
import { Form, Button, Container, Row, Col, Image } from 'react-bootstrap';
import EventService from '../services/EventService';
import VoucherService from '../services/VoucherService';
import { useNavigate } from 'react-router-dom';
import Select from 'react-select';
import './AddVoucherComponent.css';
import { useAuth } from "../AuthProvider";

function AddVoucherComponent() {
    const [voucher, setVoucher] = useState({
        code: '',
        qrCode: null,
        image: null,
        value: '',
        description: '',
        expirationDate: '',
        status: 'Active'
    });
    const [previewImage, setPreviewImage] = useState(null);
    const [previewQrCode, setPreviewQrCode] = useState(null);
    const [error, setError] = useState(null);
    const [message, setMessage] = useState(null);
    const [events, setEvents] = useState([]);
    const [selectedEvent, setSelectedEvent] = useState(null);
    const navigate = useNavigate();

    const imageInputRef = useRef(null);
    const qrCodeInputRef = useRef(null);

    const auth = useAuth();
    const brandID = auth.brand.id;

    useEffect(() => {
        const fetchEvents = async () => {
            try {
                console.log("brandID: ", brandID);

                const response = await EventService.getAllEventsByBrandId(brandID);
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
    
    const handleSubmit = async (e) => {
        e.preventDefault();

		console.log("selectedEvent: ", selectedEvent);
		console.log("voucher code: ", voucher.code);

		if (!voucher.qrCode || !voucher.image) {
            setError('Please add both QR Code and Image before submitting.');
            return;
        }

        console.log('Voucher data before submit:', voucher);

		const formData = new FormData();
        formData.append('code', voucher.code);
        formData.append('QRCode', voucher.qrCode);
        formData.append('image', voucher.image);
        formData.append('value', voucher.value);
        formData.append('description', voucher.description);
        formData.append('expirationDate', voucher.expirationDate);
        formData.append('status', voucher.status);
        formData.append('eventId', selectedEvent);

        try {
            let id = await VoucherService.createVoucher(formData);

            if (id.data === -1) {
                setError('Voucher already exist! There is another voucher with given code')
                return;
            }

            setMessage('Voucher added successfully!');
            navigate('/vouchers');
        } catch (err) {
            setError('Error adding voucher. Please try again.');
        }
    };

    return (
        <Container>
			{error && <div className="alert alert-danger mt-5">{error}</div>}
			{message && <div className="alert alert-success">{message}</div>}

            <Row className="mt-5">
                <Col md={6} className="d-flex flex-column align-items-left" style={{ paddingTop: '100px' }}>
                    <h2>Add a New Voucher</h2>
                    <p>Enter the details for the new voucher</p>
					<Form.Group controlId="formDescription">
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
                        onClick={() => qrCodeInputRef.current.click()}
                    >
                        {previewQrCode && (
                            <img 
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
                            <div className="overlay-text">Add QR Code +</div>
                        </div>
                    </div>
                    <Form.Group controlId="formFile" className="d-none">
                        <Form.Control
                            type="file"
                            name="qrCode"
                            onChange={handleChange}
                            ref={qrCodeInputRef}
                            required
                        />
                    </Form.Group>
                </Col>
            </Row>
            <Row className="mt-5">
                <Col md={6}>
                    <div
                        style={{ width: '100%', height: '320px', backgroundColor: '#F0F0F0', cursor: 'pointer', position: 'relative', overflow: 'hidden' }}
                        className="image-container mb-3"
                        onClick={() => imageInputRef.current.click()}
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
                <Col md={6}>
                    <h3>Voucher Details</h3>
                    <Form onSubmit={handleSubmit}>
                        <Form.Group controlId="formEvent">
                            <Form.Label className='label'l>Event</Form.Label>
                            <Select
                                name="event"
                                options={events}
                                className="basic-single"
                                classNamePrefix="select"
                                onChange={handleEventChange}
                                placeholder="Select an event"
								required
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
									required 
								/>
							</Form.Group>

							<Form.Group as={Col} md={6} controlId="formValue">
								<Form.Label className='label'>Value</Form.Label>
								<Form.Control 
									type="text" 
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

							<Form.Group as={Col} md={6} controlId="formStatus">
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

						</div>

                        <div className="d-flex justify-content-start mt-4" style={{gap: '12px'}}>
                            <Button variant="secondary" onClick={handleBack}>
                                <i className="bi bi-arrow-left mr-2"></i> Back
                            </Button>

                            <Button variant="primary" type="submit">
                                Save Voucher
                            </Button>
                        </div>
                    </Form>
                </Col>
            </Row>
        </Container>
    );
}

export default AddVoucherComponent;