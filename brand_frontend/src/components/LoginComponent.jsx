import React, { useState } from 'react';
import { Form, Button, Alert, Row, Col } from 'react-bootstrap';
import { useAuth } from '../AuthProvider';
const Login = () => {
    const [error, setError] = useState('');
    const [brand, setBrand] = useState({
        username: '',
        password: '',
    });
    const [errors] = useState({});
    const auth = useAuth();
    const handleLogin = (e) => {
        e.preventDefault();
        if (brand.username !== "" && brand.password !== "") {
            auth.loginAction(brand);
            return;
        }
        setError("Please provide a valid input");
    };

    const handleChange = (e) => {
        const { name, value } = e.target;
        setBrand({ ...brand, [name]: value });
    };

    return (
        <div className="mt-5">
            {error && <Alert variant="danger">{error}</Alert>}
            <div className="d-flex justify-content-center mt-5">
                <div className="p-5 gradient-custom-background border" style={{width: '40%'}}>
                    <Form onSubmit={handleLogin}>
                        <Row>
                            <Col className="left-backgroud left-form">
                                <div className="p-5">
                                    <h4 className="text-center">Sign In</h4>
                                    <Form.Group controlId="formUsername" className="mt-4">
                                        <Form.Label>
                                            Username
                                        </Form.Label>
                                        <Form.Control
                                            type="text"
                                            name="username"
                                            value={brand.username}
                                            onChange={handleChange}
                                            placeholder="Enter username"
                                            required
                                        />
                                        {errors.username && <Form.Text className="text-danger">{errors.username}</Form.Text>}
                                    </Form.Group>

                                    <Form.Group controlId="formPassword" className="mt-4">
                                        <Form.Label>
                                            Password
                                        </Form.Label>
                                        <Form.Control
                                            type="password"
                                            name="password"
                                            value={brand.password}
                                            onChange={handleChange}
                                            placeholder="Enter password"
                                            required
                                        />
                                        {errors.password && <Form.Text className="text-danger">{errors.password}</Form.Text>}
                                    </Form.Group>

                                    <div className="d-flex justify-content-center">
                                        <Button variant="primary" type="submit" className="w-50 mt-5">
                                            LOG IN
                                        </Button>
                                    </div>

                                    <div className="mt-5 text-center">
                                        Not a brand?
                                        <a href="/signup" style={{ cursor: 'pointer', color: '#007bff', textDecoration: 'underline' }}>
                                            Sign Up Now
                                        </a>
                                    </div>
                                </div>
                            </Col>
                        </Row>
                    </Form>
                </div>
            </div>
        </div>
    );
};

export default Login;
