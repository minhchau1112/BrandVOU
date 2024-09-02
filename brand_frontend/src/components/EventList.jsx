import React, { useState, useEffect } from 'react';
import EventCard from './EventCard';
import EventService from '../services/EventService';
import './EventList.css';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSearch } from '@fortawesome/free-solid-svg-icons';
import { Pagination, Button, InputGroup, FormControl, Spinner } from 'react-bootstrap';

const EventList = ({ brandID }) => {
    const [events, setEvents] = useState([]);
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(true);
    const [searchTerm, setSearchTerm] = useState('');
    const [filteredEvents, setFilteredEvents] = useState([]);
    const [page, setPage] = useState(0);
    const [rowsPerPage, setRowsPerPage] = useState(12); 
    const [totalElements, setTotalElements] = useState(0);

    useEffect(() => {
        const storedBrandId = localStorage.getItem('brandId');

        const fetchEvents = async () => {
            setLoading(true);
            setError(null);
            try {
                const response = await EventService.getEventsByBrandId(storedBrandId, page, rowsPerPage);
                setEvents(response.data.content);
                setTotalElements(response.data.totalElements);
                setFilteredEvents(response.data.content); 
                setLoading(false);
            } catch (error) {
                if (error.response) {
                    setError(`Server responded with an error: ${error.response.statusText}`);
                } else if (error.request) {
                    setError("No response from server.");
                } else {
                    setError(`Error: ${error.message}`);
                }
                setLoading(false);
            }
        };

        fetchEvents();
    }, [brandID, page, rowsPerPage]);

    const handleSearch = () => {
        const results = events.filter(event =>
            event.name.toLowerCase().includes(searchTerm.toLowerCase())
        );
        setFilteredEvents(results);
        setPage(0); 
    };

    const handleKeyDown = (event) => {
        if (event.key === 'Enter') {
            handleSearch();
        }
    };

    const handleChangePage = (newPage) => {
        if (newPage >= 0 && newPage < pageCount) {
            setPage(newPage);
        }
    };

    const handleChangeRowsPerPage = (event) => {
        setRowsPerPage(+event.target.value);
        setPage(0);
    };

    const pageCount = totalElements > 0 ? Math.ceil(totalElements / rowsPerPage) : 1;

    return (
        <div className="event-page">
            <div className="mb-4">
                <div className='d-flex align-items-center justify-content-between'>
                    <InputGroup className="search-bar">
                        <FormControl
                            placeholder="Search events..."
                            value={searchTerm}
                            onChange={(e) => setSearchTerm(e.target.value)}
                            onKeyDown={handleKeyDown}
                        />
                        <Button variant="outline-secondary" onClick={handleSearch}>
                            <FontAwesomeIcon icon={faSearch} />
                        </Button>
                    </InputGroup>
                    <div className="d-flex align-items-center" style={{gap: '12px'}}>
                        <div>Limit</div>
                        <FormControl as="select" value={rowsPerPage} onChange={handleChangeRowsPerPage}>
                            <option value={12}>12</option>
                            <option value={24}>24</option>
                            <option value={48}>48</option>
                        </FormControl>
                    </div>
                </div>
                <Button className="btn btn-primary mt-3" onClick={() => window.location.href = `/add-event`}>
                    Add event
                </Button>
            </div>
            {loading && <Spinner animation="border" />}
            {error && <div className="error-message">{error}</div>}
            <h2 className="text-center">Events List</h2>

            <div className="event-list">
                {filteredEvents.length > 0 ? (
                    filteredEvents.map(event => (
                        <EventCard key={event.id} event={event} />
                    ))
                ) : (
                    <div>No events found.</div>
                )}
            </div>
            <div className="pagination mt-4">
                <Pagination>
                    <Pagination.Prev onClick={() => handleChangePage(page > 0 ? page - 1 : 0)} />
                    {[...Array(pageCount).keys()].map(number => (
                        <Pagination.Item
                            key={number}
                            active={number === page}
                            onClick={() => handleChangePage(number)}
                        >
                            {number + 1}
                        </Pagination.Item>
                    ))}
                    <Pagination.Next onClick={() => handleChangePage(page < pageCount - 1 ? page + 1 : pageCount - 1)} />
                </Pagination>
            </div>
        </div>
    );
};

export default EventList;
