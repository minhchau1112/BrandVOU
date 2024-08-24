import React, { useState } from 'react';
import { Form, Button, Container, Alert, Row, Col } from 'react-bootstrap';
import AuthService from '../services/AuthService';
import { useNavigate } from 'react-router-dom';

const Register = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [email, setEmail] = useState('');
    const [phoneNumber, setPhoneNumber] = useState('');
    const [name, setName] = useState('');
    const [field, setField] = useState('');
    const [address, setAddress] = useState('');
    const [message, setMessage] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const handleRegister = async (e) => {
        e.preventDefault();
        try {
            await AuthService.register(username, password, email, phoneNumber, name, field, address);
            navigate('/login');
        } catch (error) {
            setError('Registration failed');
        }
    };

    return (
        <Container className='mt-5'>
            <h2 className="text-center">Register</h2>
            {error && <Alert variant="danger">{error}</Alert>}
            {message && <div className="alert alert-success">{message}</div>}
            <Row className='mt-5 justify-content-center'>
                <Col md={6}>
                    <Form onSubmit={handleRegister}>
                        <Form.Group controlId="formUsername">
                            <Form.Label>Username</Form.Label>
                            <Form.Control
                                type="text"
                                id="username"
                                value={username}
                                onChange={(e) => setUsername(e.target.value)}
                                placeholder="Enter username"
                                required
                            />
                        </Form.Group>

                        <Form.Group controlId="formPassword">
                            <Form.Label>Password</Form.Label>
                            <Form.Control
                                type="password"
                                id="password"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                placeholder="Enter password"
                                required
                            />
                        </Form.Group>

                        <Form.Group controlId="formEmail">
                            <Form.Label>Email</Form.Label>
                            <Form.Control
                                type="email"
                                id="email"
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                                placeholder="Enter email"
                                required
                            />
                        </Form.Group>

                        <Form.Group controlId="formPhoneNumber">
                            <Form.Label>Phone Number</Form.Label>
                            <Form.Control
                                type="text"
                                id="phoneNumber"
                                value={phoneNumber}
                                onChange={(e) => setPhoneNumber(e.target.value)}
                                placeholder="Enter phone number"
                                required
                            />
                        </Form.Group>

                        <Form.Group controlId="formName">
                            <Form.Label>Brand Name</Form.Label>
                            <Form.Control
                                type="text"
                                id="name"
                                value={name}
                                onChange={(e) => setName(e.target.value)}
                                placeholder="Enter brand name"
                                required
                            />
                        </Form.Group>

                        <Form.Group controlId="formField">
                            <Form.Label>Field</Form.Label>
                            <Form.Control
                                type="text"
                                id="field"
                                value={field}
                                onChange={(e) => setField(e.target.value)}
                                placeholder="Enter field"
                                required
                            />
                        </Form.Group>

                        <Form.Group controlId="formAddress">
                            <Form.Label>Address</Form.Label>
                            <Form.Control
                                type="text"
                                id="address"
                                value={address}
                                onChange={(e) => setAddress(e.target.value)}
                                placeholder="Enter address"
                                required
                            />
                        </Form.Group>

                        <Button variant="primary" type="submit" className='mt-3'>
                            Register
                        </Button>
                    </Form>
                </Col>
            </Row>
        </Container>

    );
};

export default Register;
