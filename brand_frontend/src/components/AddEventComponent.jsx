import React, {useState, useRef, useEffect} from 'react';
import { Form, Container, Image, Alert, Row, Col } from 'react-bootstrap';
import EventService from '../services/EventService';
import { useNavigate } from 'react-router-dom';
import Select from 'react-select';
import {useAuth} from "../AuthProvider";
import GameService from '../services/GameService';
import TableContainer from "@mui/material/TableContainer";
import Table from "@mui/material/Table";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import TableCell from "@mui/material/TableCell";
import TableBody from "@mui/material/TableBody";
import TextField from "@mui/material/TextField";
import IconButton from "@mui/material/IconButton";
import DeleteIcon from "@mui/icons-material/Delete";
import TablePagination from "@mui/material/TablePagination";
import Paper from "@mui/material/Paper";
import Button from '@mui/material/Button';


const columns = [
    { id: 'index', label: '#', minWidth: 50 },
    { id: 'question', label: 'Question', minWidth: 170 },
    { id: 'correctAnswer', label: 'Correct Answer', minWidth: 170 },
    { id: 'wrongAnswer1', label: 'Wrong Answer 1', minWidth: 170 },
    { id: 'wrongAnswer2', label: 'Wrong Answer 2', minWidth: 170 },
    { id: 'wrongAnswer3', label: 'Wrong Answer 3', minWidth: 170 },
    { id: 'actions', label: 'Actions', minWidth: 100 },
];

function AddEventComponent() {
    const [event, setEvent] = useState({
        name: '',
        image: null,
        voucherCount: 0,
        startTime: '',
        endTime: '',
        games: '',
        targetWord: ''
    });
    const [games, setGames] = useState([]);
    const [seletedOptions, setSelectedOptions] = useState([]);
    const [previewImage, setPreviewImage] = useState(null);
    const [error, setError] = useState(null);
    const [message, setMessage] = useState(null);
    const navigate = useNavigate();
    const [rows, setRows] = useState([{ question: '', correctAnswer: '', wrongAnswer1: '', wrongAnswer2: '', wrongAnswer3: '' }]);
    const [page, setPage] = useState(0);
    const [rowsPerPage, setRowsPerPage] = useState(10);
    const handleChangePage = (event, newPage) => {
        setPage(newPage);
    };

    const handleChangeRowsPerPage = (event) => {
        setRowsPerPage(+event.target.value);
        setPage(0);
    };

    const handleInputChange = (index, field, value) => {
        const newRows = [...rows];
        newRows[index][field] = value;
        setRows(newRows);
    };

    const handleAddRow = () => {
        setRows([...rows, { question: '', correctAnswer: '', wrongAnswer1: '', wrongAnswer2: '', wrongAnswer3: '' }]);
    };

    const handleDeleteRow = (rowIndex) => {
        const newRows = rows.filter((_, index) => index !== rowIndex);
        setRows(newRows);
    };

    useEffect(() => {
        const fetchGames = async () => {
            try {
                const response = await GameService.getAllGames();
                console.log("games:", response.data);
                const gameOptions = response.data.map(game => ({
                    value: game.id,
                    label: game.name,
                    isItemExchangeAllowed: game.isItemExchangeAllowed
                }));
                setGames(gameOptions);
            } catch (error) {
                setError('Error fetching games.');
            }
        };

        fetchGames();
    }, []);

    const imageInputRef = useRef(null);
    const auth = useAuth();
    const brandID = auth.brand.id;

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

    const handleGamesChange = (selectedOptions) => {
        console.log("selectedOptions: ", selectedOptions);
        const games = selectedOptions.map(option => option.value).join(';');
        setEvent({ ...event, games: games });
        setSelectedOptions(selectedOptions);
    };

    const handleBack = () => {
        navigate(-1);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (new Date(event.startTime) >= new Date(event.endTime)) {
            setError('End time must be after start time.');
            return;
        }

        if (!event.image) {
            setError('Please add Image before submitting.');
            return;
        }

        console.log('Event data before submit:', event);

        console.log("brandID: ", typeof brandID);

        const formData = new FormData();
        formData.append('brandId', brandID);
        formData.append('name', event.name);
        formData.append('image', event.image);
        formData.append('voucherCount', event.voucherCount);
        formData.append('startTime', event.startTime);
        formData.append('endTime', event.endTime);
        formData.append('games', event.games);

        if (seletedOptions.some(game => game.isItemExchangeAllowed === true)) {
            formData.append('targetWord', event.targetWord);
        }

        formData.append('questions', JSON.stringify(rows));

        try {
            let id = await EventService.createEvent(formData);

            if (id.data === -1) {
                setError('Event already exist! There is another event with given name')
                return;
            }

            setMessage("Event created successfully!");
            navigate('/');
        } catch (err) {
            setError('There was an error creating the event. Please try again.');
        }
    }
    return (
        <Container className='mt-5'>
            <h2 className="text-center">Add New Event</h2>
            {error && <Alert variant="danger">{error}</Alert>}
			{message && <div className="alert alert-success">{message}</div>}
            <Form onSubmit={handleSubmit}>
            <Row className='mt-5'>
                <Col md={6}>
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

                        <Form.Group controlId="formvoucherCount" className="mt-1">
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

                        <div className='d-flex justify-content-between mt-1'>
                            <Form.Group as={Col} md={5} controlId="formStartTime">
                                <Form.Label>Start Time</Form.Label>
                                <Form.Control 
                                    type="datetime-local" 
                                    name="startTime" 
                                    value={event.startTime} 
                                    onChange={handleChange} 
                                    required 
                                />
                            </Form.Group>

                            <Form.Group as={Col} md={5} controlId="formEndTime">
                                <Form.Label>End Time</Form.Label>
                                <Form.Control 
                                    type="datetime-local" 
                                    name="endTime" 
                                    value={event.endTime} 
                                    onChange={handleChange} 
                                    required 
                                />
                            </Form.Group>
                        </div>
                        

                        <Form.Group controlId="formGameType" className="mt-1">
                            <Form.Label>Games</Form.Label>

                            <Select
                                isMulti
                                name="games"
                                options={games}
                                className="basic-multi-select"
                                classNamePrefix="select"
                                onChange={handleGamesChange}
                                required
                            />
                        </Form.Group>

                        {seletedOptions.some(game => game.isItemExchangeAllowed === true) && (
                            <Form.Group controlId="formTargetWord" className="mt-1">
                                <Form.Label>Target Word</Form.Label>
                                <Form.Control
                                    type="text"
                                    name="targetWord"
                                    value={event.targetWord}
                                    onChange={handleChange}
                                    placeholder="Enter target word for ShakeGame"
                                />
                            </Form.Group>
                        )}

                </Col>
                <Col md={6}>
                    <div
                        style={{ width: '100%', height: '312px', backgroundColor: '#F0F0F0', cursor: 'pointer', position: 'relative', overflow: 'hidden' }}
                        className="image-container mb-3"
                        onClick={() => imageInputRef.current.click()}
                    >
                        {previewImage && (
                            <Image 
                                src={previewImage} 
                                alt="Event Image Preview" 
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
                    <Form.Group controlId="formFile">
                        <Form.Control
                            type="file"
                            name="image"
                            onChange={handleChange}
                            ref={imageInputRef} 
                            required
                        />
                    </Form.Group>
                </Col>
            </Row>

            {seletedOptions.some(game => game.isItemExchangeAllowed === false) && (
                <Paper className="mt-4" sx={{ width: '100%', overflow: 'hidden', padding: 2 }}>
                    <h3>List Question for Quiz Game</h3>
                    <TableContainer sx={{ maxHeight: 440 }}>
                        <Table stickyHeader aria-label="sticky table">
                            <TableHead>
                                <TableRow>
                                    {columns.map((column) => (
                                        <TableCell
                                            key={column.id}
                                            style={{ minWidth: column.minWidth }}
                                        >
                                            {column.label}
                                        </TableCell>
                                    ))}
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {rows.slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage).map((row, rowIndex) => (
                                    <TableRow hover role="checkbox" tabIndex={-1} key={rowIndex}>
                                        {/* Serial number column */}
                                        <TableCell>{rowIndex + 1 + page * rowsPerPage}</TableCell>
                                        {columns.slice(1, -1).map((column) => (
                                            <TableCell key={column.id}>
                                                <TextField
                                                    fullWidth
                                                    variant="outlined"
                                                    value={row[column.id]}
                                                    onChange={(e) => handleInputChange(rowIndex, column.id, e.target.value)}
                                                />
                                            </TableCell>
                                        ))}
                                        {/* Actions column for delete button */}
                                        <TableCell>
                                            <IconButton
                                                style={{ color: 'red' }}
                                                onClick={() => handleDeleteRow(rowIndex)}
                                            >
                                                <DeleteIcon />
                                            </IconButton>
                                        </TableCell>
                                    </TableRow>
                                ))}
                            </TableBody>
                        </Table>
                    </TableContainer>
                    <TablePagination
                        rowsPerPageOptions={[10, 25, 100]}
                        component="div"
                        count={rows.length}
                        rowsPerPage={rowsPerPage}
                        page={page}
                        onPageChange={handleChangePage}
                        onRowsPerPageChange={handleChangeRowsPerPage}
                    />
                    <Button variant="contained" color="secondary" onClick={handleAddRow} sx={{ marginTop: 2 }}>
                        Add Row +
                    </Button>
                </Paper>
            )}

            <div className="d-flex justify-content-start mt-4 mb-5" style={{gap: '12px'}}>
                <Button variant="contained" color="inherit" onClick={handleBack}>
                    <i className="bi bi-arrow-left mr-2"></i> Back
                </Button>

                <Button variant="contained" color="primary" type="submit">
                    Save Event
                </Button>
            </div>
            </Form>
        </Container>
    );
}

export default AddEventComponent;