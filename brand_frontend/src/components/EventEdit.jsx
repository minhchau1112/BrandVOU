import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Table, Form, Container, Button, Spinner, Alert, Image } from 'react-bootstrap';
import Select from 'react-select';
import EventService from '../services/EventService';
import VoucherService from '../services/VoucherService';

function EditEvent() {
    const { id } = useParams();
    const navigate = useNavigate();
    const [event, setEvent] = useState({
        name: '',
        image: '',
        startTime: '',
        endTime: '',
        gameType: '',
        voucherCount: 0
    });
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [previewImage, setPreviewImage] = useState(null);
    const [vouchers, setVouchers] = useState([]);

    const gameOptions = [
        { value: 'Quiz', label: 'Quiz' },
        { value: 'ShakeGame', label: 'ShakeGame' }
    ];

    useEffect(() => {
        const fetchEventDetail = async () => {
            try {
                const response = await EventService.getEventByEventId(id);
                const eventData = response.data;

                const selectedGameTypes = eventData.gameType
                    ? eventData.gameType.split(';').map(type => ({
                          value: type,
                          label: type
                      }))
                    : [];

                setEvent({
                    ...eventData,
                    gameType: selectedGameTypes
                });
                setPreviewImage(eventData.image);
                setLoading(false);
            } catch (error) {
                setError('Error fetching event details.');
                setLoading(false);
            }
        };
        fetchEventDetail();

		const fetchVouchers = async () => {
            try {
                const response = await VoucherService.getVoucherByEventId(id);
                setVouchers(response.data);
                setLoading(false);
            } catch (error) {
                setError('Error fetching vouchers.');
                setLoading(false);
            }
        };

        fetchVouchers();
    }, [id]);

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
        setEvent({ ...event, gameType: selectedOptions });
    };

	const handleViewDetailVoucher = (voucherId) => {
        navigate(`/vouchers/view-detail/${voucherId}`);
    };

    const handleEditVoucher = async (voucherId) => {
        navigate(`/vouchers/edit/${voucherId}`);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const updatedEvent = {
                ...event,
                gameType: event.gameType.map(option => option.value).join(';')
            };

            await EventService.updateEvent(updatedEvent, id);
            navigate(`/events/view/${id}`);
        } catch (error) {
            setError('Error updating event.');
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
            <Form onSubmit={handleSubmit}>
                <Form.Group>
                    <Form.Label>Event Name</Form.Label>
                    <Form.Control
                        type="text"
                        name="name"
                        value={event.name}
                        onChange={handleChange}
                        required
                    />
                </Form.Group>

                <Form.Group controlId="formEventImage">
                    <Form.Label>Event Image</Form.Label>
                    <Form.Control
                        type="file"
                        name="image"
                        onChange={handleChange}
                    />
                    {previewImage && (
                        <div className="mt-3">
                            <Image src={previewImage} alt="Event Preview" thumbnail style={{ maxWidth: '200px' }} />
                        </div>
                    )}
                </Form.Group>

                <Form.Group>
                    <Form.Label>Start Time</Form.Label>
                    <Form.Control
                        type="datetime-local"
                        name="startTime"
                        value={event.startTime}
                        onChange={handleChange}
                        required
                    />
                </Form.Group>

                <Form.Group>
                    <Form.Label>End Time</Form.Label>
                    <Form.Control
                        type="datetime-local"
                        name="endTime"
                        value={event.endTime}
                        onChange={handleChange}
                        required
                    />
                </Form.Group>

                <Form.Group>
                    <Form.Label>Game Type</Form.Label>
                    <Select
                        isMulti
                        name="gameType"
                        options={gameOptions}
                        className="basic-multi-select"
                        classNamePrefix="select"
                        value={event.gameType}
                        onChange={handleGameTypeChange}
                    />
                </Form.Group>

                <Form.Group>
                    <Form.Label>Number of Vouchers</Form.Label>
                    <Form.Control
                        type="number"
                        name="voucherCount"
                        value={event.voucherCount}
                        onChange={handleChange}
                        required
                    />
                </Form.Group>

				<Form.Group>
					<Form.Label>List voucher</Form.Label>
					{vouchers.length > 0 ? (
					<Table striped bordered hover>
						<thead>
							<tr>
								<th>Code</th>
								<th>QR Code</th>
								<th>Image</th>
								<th>Value</th>
								<th>Description</th>
								<th>Expiration Date</th>
								<th>Status</th>
								<th>Event Name</th>
								<th>Action</th> 
							</tr>
						</thead>
						<tbody>
							{vouchers.map(voucher => (
								<tr key={voucher.id}>
									<td>{voucher.code}</td>
									<td>
										<a href={voucher.qrcode} target="_blank" rel="noopener noreferrer">
											{voucher.qrcode.length > 30 ? `${voucher.qrcode.slice(0, 30)}...` : voucher.qrcode}
										</a>
									</td>
									<td>
										<a href={voucher.image} target='_blank' rel="nooperner noreferrer">
											{voucher.image.length > 30 ? `${voucher.image.slice(0, 30)}...` : voucher.image}
										</a>
									</td>
									<td>{voucher.value}</td>
									<td>{voucher.description.length > 50 ? `${voucher.description.slice(0, 50)}...` : voucher.description}</td>
									<td>{new Date(voucher.expirationDate).toLocaleString()}</td>
									<td>{voucher.status}</td>
									<td>{voucher.event.name}</td>
									<td>
										<div
											className="icon-container d-flex"
											style={{
												justifyContent: 'space-between',
												alignItems: 'center',
												gap: '10px',
												padding: '5px',
												cursor: 'pointer'
											}}
										>
											<i
												className="bi bi-eye"
												style={{ color: 'blue'}}
												onClick={() => handleViewDetailVoucher(voucher.id)}
											></i>
											<i
												className="bi bi-pencil"
												style={{ color: 'green'}}
												onClick={() => handleEditVoucher(voucher.id)}
											></i>
										</div>
									</td>
								</tr>
							))}
						</tbody>
					</Table>
				) : (
					!loading && <Alert variant="info">No vouchers found for this event.</Alert>
				)}
				</Form.Group>

                <Button variant="primary" type="submit" className="mt-3">
                    Save Changes
                </Button>
            </Form>
        </Container>
    );
}

export default EditEvent;