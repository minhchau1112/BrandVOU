import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import {
    Container,
    Alert,
    Spinner,
    Form,
    Image,
    Button,
    Col,
    Row,
} from 'react-bootstrap';

import ItemService from "../services/ItemService";

function ItemDetail() {
    const { id } = useParams();
    const navigate = useNavigate();
    const [item, setItem] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchItemDetail = async () => {
            try {
                const response = await ItemService.getItemByItemId(id);
                console.log("item: ", response.data);
                setItem(response.data);

                setLoading(false);
            } catch (error) {
                setError('Error fetching item.');
                setLoading(false);
            }
        };

        fetchItemDetail();
    }, [id]);

    const handleEdit = () => {
        navigate(`/items/edit/${id}`);
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
        <Container className="mt-5">
            <h2 className="text-center">Item Detail</h2>
            <Row className="mt-5">
                <Col md={6}>
                    <Form>
                        <Form.Group controlId="formItemName">
                            <Form.Label>Item Name</Form.Label>
                            <Form.Control
                                type="text"
                                value={item?.name}
                                disabled
                            />
                        </Form.Group>

                        <Form.Group controlId="formEventName">
                            <Form.Label>Event Name</Form.Label>
                            <Form.Control
                                type="text"
                                value={item?.event.name}
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

                            <Button variant="primary" onClick={handleEdit}>
                                <i className="bi bi-pencil mr-2"></i> Edit
                            </Button>
                        </div>
                    </Form>
                </Col>
                <Col md={6}>
                    <div
                        style={{ width: '100%', height: '320px', backgroundColor: '#F0F0F0', position: 'relative', overflow: 'hidden' }}
                        className="image-container mb-3"
                    >
                        {item?.image && (
                            <Image
                                src={item.image}
                                alt="Item Image"
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

        </Container>
    );
}

export default ItemDetail;
