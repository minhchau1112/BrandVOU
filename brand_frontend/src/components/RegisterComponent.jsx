import React, { useState } from 'react';
import { Form, Button, Row, Col, Alert } from 'react-bootstrap';
import AuthService from '../services/AuthService';
import { useNavigate } from 'react-router-dom';
import './RegisterComponent.css';
import {HttpStatusCode} from "axios";

const Register = () => {
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const [brand, setBrand] = useState({
        username: '',
        password: '',
        email: '',
        phoneNumber: '',
        role: 'BRAND',
        status: 'ACTIVE',
        name: '',
        field: '',
        address: '',
        gpsLat: '',
        gpsLong: ''
    });

    const [errors, setErrors] = useState({});

    const validateForm = () => {
        let formErrors = {};

        if (!brand.username || brand.username.length < 8) {
            formErrors.username = "Minimum username length is 8 characters.";
        }

        if (!brand.password || brand.password.length < 8) {
            formErrors.password = "Minimum password length is 8 characters.";
        }

        if (!brand.email || brand.email.length < 7) {
            formErrors.email = "Minimum e-mail length is 7 characters.";
        }

        if (!brand.phoneNumber || brand.phoneNumber.length < 11 || brand.phoneNumber.length > 20) {
            formErrors.phoneNumber = "Phone number must be between 11 and 20 characters.";
        } else if (!validatePhoneNumber(brand.phoneNumber)) {
            formErrors.phoneNumber = "Your phone number is invalid!";
        }

        if (!brand.name) {
            formErrors.name = "Brand Name can't be blank.";
        }

        if (!brand.field) {
            formErrors.field = "Field can't be blank.";
        }

        if (!brand.address) {
            formErrors.address = "Address can't be blank.";
        }

        const gpsLat = parseFloat(brand.gpsLat);
        if (isNaN(gpsLat)) {
            formErrors.gpsLat = "GPS_lat can't be blank and must be a valid number.";
        } else if (gpsLat < -90 || gpsLat > 90) {
            formErrors.gpsLat = "GPS_lat must be between -90 and 90.";
        } else {
            setBrand({ ...brand, [gpsLat]: gpsLat });
        }

        const gpsLong = parseFloat(brand.gpsLong);
        if (isNaN(gpsLong)) {
            formErrors.gpsLong = "GPS_long can't be blank and must be a valid number.";
        } else if (gpsLong < -180 || gpsLong > 180) {
            formErrors.gpsLong = "GPS_long must be between -180 and 180.";
        } else {
            setBrand({ ...brand, [gpsLong]: gpsLong });
        }

        setErrors(formErrors);
        return Object.keys(formErrors).length === 0;
    };

    const handleChange = (e) => {
        const { name, value } = e.target;
        setBrand({ ...brand, [name]: value });
    };

    const handleRegister = async (e) => {
        e.preventDefault();

        if (!validateForm()) {
            return;
        }

        let requestBody = JSON.stringify(brand);
        await AuthService.registerBrand(requestBody).then(res => {
            const status = res.status;
            console.log("status: ", status);
            if (status === HttpStatusCode.Ok) {
                console.log("body: ", res.data.message);
                setError(res.data.message);
            } else if (status === HttpStatusCode.Created){
                console.log("data: ", res);
                navigate('/login');
            }
        }).catch(error => {
            console.log("error: ", error);
            setError("Register Failed! Error: " + error);
        });
    };

    const validatePhoneNumber = (phoneNumber) => {
        const phoneRegex = /(?:\+84|0084|0)[235789][0-9]{1,2}[0-9]{7}(?:[^\d]+|$)/g;
        return phoneRegex.test(phoneNumber);
    };

    return (
        <div className="mt-5">
            {error && <Alert variant="danger">{error}</Alert>}
            <div className="d-flex justify-content-center mt-5">
                <div className="w-100 p-5 gradient-custom-background border">
                    <Form onSubmit={handleRegister}>
                        <Row>
                            <Col md={6} className="left-backgroud left-form">
                                <div className="p-4">
                                    <h4>General Information</h4>
                                    <Form.Group controlId="formUsername" className="mt-4">
                                        <Form.Label>
                                            Username
                                            <span className="text-danger"> *</span>
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
                                            <span className="text-danger"> *</span>
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

                                    <Form.Group controlId="formEmail" className="mt-4">
                                        <Form.Label>
                                            Email
                                            <span className="text-danger"> *</span>
                                        </Form.Label>
                                        <Form.Control
                                            type="email"
                                            name="email"
                                            value={brand.email}
                                            onChange={handleChange}
                                            placeholder="Enter email"
                                            required
                                        />
                                        {errors.email && <Form.Text className="text-danger">{errors.email}</Form.Text>}
                                    </Form.Group>

                                    <Form.Group controlId="formPhoneNumber" className="mt-4">
                                        <Form.Label>
                                            Phone Number
                                            <span className="text-danger"> *</span>
                                        </Form.Label>
                                        <Form.Control
                                            type="text"
                                            name="phoneNumber"
                                            value={brand.phoneNumber}
                                            onChange={handleChange}
                                            placeholder="Enter phone number"
                                            required
                                        />
                                        {errors.phoneNumber && <Form.Text className="text-danger">{errors.phoneNumber}</Form.Text>}
                                    </Form.Group>
                                </div>
                            </Col>

                            <Col md={6} className="right-background right-form">
                                <div className="p-4">
                                    <h4 className="text-white">Brand Details</h4>
                                    <Form.Group controlId="formName" className="mt-4">
                                        <Form.Label className="text-white">
                                            Brand Name
                                            <span className="text-danger"> *</span>
                                        </Form.Label>
                                        <Form.Control
                                            type="text"
                                            name="name"
                                            value={brand.name}
                                            onChange={handleChange}
                                            placeholder="Enter Your Brand Name"
                                            required
                                        />
                                        {errors.name && <Form.Text className="text-danger">{errors.name}</Form.Text>}
                                    </Form.Group>

                                    <Form.Group controlId="formField" className="mt-4">
                                        <Form.Label className="text-white">
                                            Field
                                            <span className="text-danger"> *</span>
                                        </Form.Label>
                                        <Form.Control
                                            type="text"
                                            name="field"
                                            value={brand.field}
                                            onChange={handleChange}
                                            placeholder="Enter Your Field"
                                            required
                                        />
                                        {errors.field && <Form.Text className="text-danger">{errors.field}</Form.Text>}
                                    </Form.Group>

                                    <Form.Group controlId="formAddress" className="mt-4">
                                        <Form.Label className="text-white">
                                            Address
                                            <span className="text-danger"> *</span>
                                        </Form.Label>
                                        <Form.Control
                                            type="text"
                                            name="address"
                                            value={brand.address}
                                            onChange={handleChange}
                                            placeholder="Enter Your Address"
                                            required
                                        />
                                        {errors.address && <Form.Text className="text-danger">{errors.address}</Form.Text>}
                                    </Form.Group>

                                    <Row className="mt-4">
                                        <Col>
                                            <Form.Group controlId="formGPSLat">
                                                <Form.Label className="text-white">
                                                    GPS Lat
                                                    <span className="text-danger"> *</span>
                                                </Form.Label>
                                                <Form.Control
                                                    type="text"
                                                    name="gpsLat"
                                                    value={brand.gpsLat}
                                                    onChange={handleChange}
                                                    placeholder="Enter Your GPS Lat"
                                                    required
                                                />
                                                {errors.gpsLat && <Form.Text className="text-danger">{errors.gpsLat}</Form.Text>}
                                            </Form.Group>
                                        </Col>

                                        <Col>
                                            <Form.Group controlId="formGPSLong">
                                                <Form.Label className="text-white">
                                                    GPS Long
                                                    <span className="text-danger"> *</span>
                                                </Form.Label>
                                                <Form.Control
                                                    type="text"
                                                    name="gpsLong"
                                                    value={brand.gpsLong}
                                                    onChange={handleChange}
                                                    placeholder="Enter Your GPS Long"
                                                    required
                                                />
                                                {errors.gpsLong && <Form.Text className="text-danger">{errors.gpsLong}</Form.Text>}
                                            </Form.Group>
                                        </Col>
                                    </Row>

                                    <Button variant="light" type="submit" className="w-25 mt-5">
                                        REGISTER
                                    </Button>
                                </div>
                            </Col>
                        </Row>
                    </Form>
                </div>
            </div>
        </div>
    );
};

export default Register;