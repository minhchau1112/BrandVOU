import React, { useState } from 'react';
import { Form, Button, Container, Image, Alert } from 'react-bootstrap';
import EventService from '../services/EventService';
import { useNavigate } from 'react-router-dom';
import Select from 'react-select';

function AddEventComponent({brandID}) {
    const [event, setEvent] = useState({
        name: '',
        image: null,
        voucherCount: 0,
        startTime: '',
        endTime: '',
        gameType: ''
    });
    const [previewImage, setPreviewImage] = useState(null);
    const [error, setError] = useState(null);
    const [setMessage] = useState(null);
    const navigate = useNavigate();

    const gameOptions = [
        { value: 'Quiz', label: 'Quiz' },
        { value: 'ShakeGame', label: 'ShakeGame' }
    ];

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
        const gameTypes = selectedOptions.map(option => option.value).join(';');
        setEvent({ ...event, gameType: gameTypes });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (new Date(event.startTime) >= new Date(event.endTime)) {
            setError('End time must be after start time.');
            return;
        }

        const formData = new FormData();
        formData.append('name', event.name);
        formData.append('image', event.image);
        formData.append('voucherCount', event.voucherCount);
        formData.append('startTime', event.startTime);
        formData.append('endTime', event.endTime);
        formData.append('gameType', event.gameType); 

        try {
            await EventService.createEvent(formData, brandID);
            setMessage("Event created successfully!");
            navigate('/');
        } catch (err) {
            setError('There was an error creating the event. Please try again.');
        };
    }
    return (
        <Container className='mt-5'>
            <h2 className="text-center">Add New Event</h2>
            {error && <Alert variant="danger">{error}</Alert>}
            <Form onSubmit={handleSubmit}>
                <Form.Group controlId="formEventName">
                    <Form.Label>Event Name</Form.Label>
                    <Form.Control 
                        type="text" 
                        name="name" 
                        value={event.name} 
                        onChange={handleChange} 
                        placeholder="Enter event name" 
                        required 
                    />
                </Form.Group>

                <Form.Group controlId="formEventImage">
                    <Form.Label>Event Image</Form.Label>
                    <Form.Control 
                        type="file" 
                        name="image" 
                        onChange={handleChange} 
                        required 
                    />
                    {previewImage && (
                        <div className="mt-3">
                            <Image src={previewImage} alt="Event Preview" thumbnail style={{ maxWidth: '200px' }} />
                        </div>
                    )}
                </Form.Group>

                <Form.Group controlId="formvoucherCount">
                    <Form.Label>Number of Vouchers</Form.Label>
                    <Form.Control 
                        type="number" 
                        name="voucherCount" 
                        value={event.voucherCount} 
                        onChange={handleChange} 
                        placeholder="Enter number of vouchers" 
                        required 
                    />
                </Form.Group>

                <Form.Group controlId="formStartTime">
                    <Form.Label>Start Time</Form.Label>
                    <Form.Control 
                        type="datetime-local" 
                        name="startTime" 
                        value={event.startTime} 
                        onChange={handleChange} 
                        required 
                    />
                </Form.Group>

                <Form.Group controlId="formEndTime">
                    <Form.Label>End Time</Form.Label>
                    <Form.Control 
                        type="datetime-local" 
                        name="endTime" 
                        value={event.endTime} 
                        onChange={handleChange} 
                        required 
                    />
                </Form.Group>

                <Form.Group controlId="formGameType">
                    <Form.Label>Game Type</Form.Label>
                    <Select
                        isMulti
                        name="gameType"
                        options={gameOptions}
                        className="basic-multi-select"
                        classNamePrefix="select"
                        onChange={handleGameTypeChange}
                    />
                </Form.Group>
                
                <Button variant="primary" type="submit" className='mt-3'>
                    Submit
                </Button>
            </Form>
        </Container>
    );
}

export default AddEventComponent;