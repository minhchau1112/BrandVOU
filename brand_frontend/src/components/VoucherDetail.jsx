import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Form, Button, Container, Row, Col, Alert, Spinner, Image } from 'react-bootstrap';
import VoucherService from '../services/VoucherService';
import './AddVoucherComponent.css';

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

    const handleBack = () => {
        navigate(-1);
    };

    if (loading) {
        return <Spinner animation="border" />;
    }

    if (error) {
        return <Alert variant="danger">{error}</Alert>;
    }

    return (
        <Container>
            <Row className="mt-5">
                <Col md={6} className="d-flex flex-column align-items-left" style={{ paddingTop: '50px' }}>
                    <h2>Voucher Detail</h2>
                    <p>View the details for the voucher</p>
					<Form.Group controlId="formDescription">
                        <Form.Label className='label'>Description</Form.Label>
						<Form.Control 
							type="text" 
							value={voucher?.description} 
                            disabled
						/>
					</Form.Group>
                </Col>
                <Col md={6} className="d-flex flex-column align-items-center">
                    <div
                        style={{ width: '100%', height: '300px', backgroundColor: '#F0F0F0', position: 'relative', overflow: 'hidden' }}
                        className="image-container mb-3"
                    >
                        {voucher?.qrcode && (
                            <Image
                                src={voucher.qrcode}
                                alt="QR Code"
                                thumbnail
                                style={{
                                    width: '100%',
                                    height: '100%',
                                    objectFit: 'contain'
                                }}
                            />
                        )}
                    </div>
                </Col>
            </Row>
            <Row className="mt-5">
                <Col md={6}>
                    <div
                        style={{ width: '100%', height: '320px', backgroundColor: '#F0F0F0', position: 'relative', overflow: 'hidden' }}
                        className="image-container mb-3"
                    >
                        {voucher?.image && (
                            <Image
                                src={voucher.image}
                                alt="Voucher Image"
                                thumbnail
                                style={{
                                    width: '100%',
                                    height: '100%',
                                    objectFit: 'contain'
                                }}
                            />
                        )}
                    </div>
                </Col>
                <Col md={6}>
                    <h3>Voucher Details</h3>
                    <Form>
                        <Form.Group controlId="formEvent">
                            <Form.Label className='label'>Event Name</Form.Label>
                            <Form.Control type="text" value={voucher?.event.name} disabled />
                         </Form.Group>
                        <div className='d-flex justify-content-between mt-1'>
                            <Form.Group as={Col} md={5} controlId="formCode">
                                <Form.Label className='label'>Code</Form.Label>
                                <Form.Control 
                                    type="text" 
                                    value={voucher.code} 
                                    disabled 
                                />
                            </Form.Group>

                            <Form.Group as={Col} md={6} controlId="formValue">
                                <Form.Label className='label'>Value</Form.Label>
                                <Form.Control 
                                    type="number" 
                                    value={voucher.value} 
                                    disabled
                                />
                            </Form.Group>
                        </div>

                        <div className='d-flex justify-content-between mt-1'>
                            <Form.Group as={Col} md={5} controlId="formExpirationDate">
                                <Form.Label className='label'>Expiration Date</Form.Label>
                                <Form.Control 
                                    type="datetime-local" 
                                    value={voucher.expirationDate} 
                                    disabled 
                                />
                            </Form.Group>

                            <Form.Group as={Col} md={3} controlId="formStatus">
                                <Form.Label className='label'>Status</Form.Label>
                                <Form.Control type="text" value={voucher?.status} disabled />
                            </Form.Group>

                            <Form.Group as={Col} md={3} controlId="formCount">
                                <Form.Label className='label'>Number of voucher</Form.Label>
                                <Form.Control 
                                    type="number" 
                                    value={voucher.count} 
                                    disabled
                                >
                                </Form.Control>
                            </Form.Group>
                        </div>

                        <div className="d-flex justify-content-start mt-3" style={{gap: '12px'}}>
                            <Button variant="secondary" onClick={handleBack}>
                                <i className="bi bi-arrow-left mr-2"></i> Back
                            </Button>

                            <Button variant="primary" onClick={handleEdit}>
                                <i className="bi bi-pencil mr-2"></i> Edit
                            </Button>
                        </div>

                        {/*<Button variant="secondary" onClick={handleBack} className="mr-2 mt-3">*/}
                        {/*    <i className="bi bi-arrow-left mr-2"></i> Back*/}
                        {/*</Button>*/}
                    </Form>
                </Col>
            </Row>
        </Container>
    );
}

export default VoucherDetail;
