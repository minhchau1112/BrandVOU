import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Container, Alert, Spinner, Form, Image, Button } from 'react-bootstrap';
import VoucherService from '../services/VoucherService';

function VoucherDetail() {
    const { id } = useParams();
    const navigate = useNavigate();
    const [voucher, setVoucher] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchVoucherDetails = async () => {
            try {
                const response = await VoucherService.getVoucherByVoucherId(id);
                setVoucher(response.data);
                setLoading(false);
            } catch (error) {
                setError('Error fetching voucher details.');
                setLoading(false);
            }
        };

        fetchVoucherDetails();
    }, [id]);

    const handleEdit = () => {
        navigate(`/vouchers/edit/${id}`);
    };

    const handleDelete = async () => {
        try {
            await VoucherService.deleteVoucher(id);
            navigate('/vouchers');
        } catch (error) {
            setError('Error deleting voucher.');
        }
    };

    if (loading) {
        return <Spinner animation="border" />;
    }

    if (error) {
        return <Alert variant="danger">{error}</Alert>;
    }

    return (
        <Container className='mt-5'>
            <h2 className="text-center">Voucher Detail</h2>
            {voucher ? (
                <Form>
                    <Form.Group>
                        <Form.Label>Code</Form.Label>
                        <Form.Control type="text" value={voucher.code} disabled />
                    </Form.Group>

                    <Form.Group>
                        <Form.Label>QR Code</Form.Label>
                        <div>
                            <Image src={voucher.qrcode} alt="QR Code" thumbnail style={{ maxWidth: '200px' }} />
                        </div>
                    </Form.Group>

                    <Form.Group>
                        <Form.Label>Description</Form.Label>
                        <Form.Control type="text" value={voucher.description} disabled />
                    </Form.Group>

                    <Form.Group>
                        <Form.Label>Expiration Date</Form.Label>
                        <Form.Control
                            type="text"
                            value={new Date(voucher.expirationDate).toLocaleString()}
                            disabled
                        />
                    </Form.Group>

                    <Form.Group>
                        <Form.Label>Image</Form.Label>
                        <div>
                            <Image src={voucher.image} alt="Voucher Image" thumbnail style={{ maxWidth: '200px' }} />
                        </div>
                    </Form.Group>

                    <Form.Group>
                        <Form.Label>Status</Form.Label>
                        <Form.Control type="text" value={voucher.status} disabled />
                    </Form.Group>

                    <Form.Group>
                        <Form.Label>Value</Form.Label>
                        <Form.Control type="text" value={voucher.value} disabled />
                    </Form.Group>

                    <Form.Group>
                        <Form.Label>Event Name</Form.Label>
                        <Form.Control type="text" value={voucher.event.name} disabled />
                    </Form.Group>

                    <div className="d-flex justify-content-end mt-4" 
						style={{
							justifyContent: 'space-between',
							alignItems: 'center',
							gap: '10px',
							padding: '5px',
							cursor: 'pointer'
						}}>
                        <Button variant="primary" onClick={handleEdit}>
                            <i className="bi bi-pencil mr-2"></i> Edit
                        </Button>
                        <Button variant="danger" onClick={handleDelete}>
                            <i className="bi bi-trash3 mr-2"></i> Delete
                        </Button>
                    </div>
                </Form>
            ) : (
                <Alert variant="info">No voucher details available.</Alert>
            )}
        </Container>
    );
}

export default VoucherDetail;