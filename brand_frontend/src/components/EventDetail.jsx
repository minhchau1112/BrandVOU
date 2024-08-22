import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Table, Container, Alert, Spinner, Form, Image, Button } from 'react-bootstrap';
import EventService from '../services/EventService';
import VoucherService from '../services/VoucherService';

function EventDetail() {
    const { id } = useParams();
    const navigate = useNavigate();
    const [event, setEvent] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [vouchers, setVouchers] = useState([]);

    useEffect(() => {
        const fetchEventDetail = async () => {
            try {
                const response = await EventService.getEventByEventId(id);
                setEvent(response.data);
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

    const handleEdit = () => {
        navigate(`/events/edit/${id}`);
    };

    const handleViewDetailVoucher = (voucherId) => {
        navigate(`/vouchers/view-detail/${voucherId}`);
    };

    const handleEditVoucher = async (voucherId) => {
        navigate(`/vouchers/edit/${voucherId}`);
    };

    if (loading) {
        return <Spinner animation="border" />;
    }

    if (error) {
        return <Alert variant="danger">{error}</Alert>;
    }

    return (
        <Container className='mt-5'>
            <h2 className="text-center">Event Detail</h2>
            {event ? (
                <Form>
                    <Form.Group>
                        <Form.Label>Event Name</Form.Label>
                        <Form.Control type="text" value={event.name} disabled />
                    </Form.Group>

                    <Form.Group>
                        <Form.Label>Event Image</Form.Label>
                        <div>
                            <Image src={event.image} alt="Event Image" thumbnail style={{ maxWidth: '200px' }} />
                        </div>
                    </Form.Group>

                    <Form.Group>
                        <Form.Label>Number of vouchers</Form.Label>
                        <Form.Control type="number" value={event.voucherCount} disabled />
                    </Form.Group>

                    <Form.Group>
                        <Form.Label>Start Time</Form.Label>
                        <Form.Control
                            type="datetime-local"
                            value={event.startTime}
                            disabled
                        />
                    </Form.Group>

					<Form.Group>
                        <Form.Label>End Time</Form.Label>
                        <Form.Control
                            type="datetime-local"
                            value={event.endTime}
                            disabled
                        />
                    </Form.Group>

					<Form.Group>
                        <Form.Label>Game Type</Form.Label>
                        <Form.Control
                            type="text"
                            value={event.gameType.replace(';', ', ')}
                            disabled
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

					<Button variant="primary" className='mt-3' onClick={handleEdit}>
						<i className="bi bi-pencil mr-2"></i> Edit
					</Button>
                </Form>
            ) : (
                <Alert variant="info">No event details available.</Alert>
            )}
        </Container>
    );
}

export default EventDetail;