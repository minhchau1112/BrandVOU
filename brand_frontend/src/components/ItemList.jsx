import React, { useState, useEffect, useCallback } from 'react';
import ItemService from '../services/ItemService';
import './EventList.css';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSearch } from '@fortawesome/free-solid-svg-icons';
import { Pagination, Button, InputGroup, FormControl, Spinner } from 'react-bootstrap';
import { useAuth } from "../AuthProvider";
import ItemCard from "./ItemCard";

const ItemList = ({ brandID }) => {
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(true);
    const [searchTerm, setSearchTerm] = useState('');
    const [filteredItems, setFilteredItems] = useState([]);
    const [pageNumber, setPageNumber] = useState(0);
    const [pageSize, setPageSize] = useState(24);
    const [totalElements, setTotalElements] = useState(0);
    const auth = useAuth();
    brandID = auth.brand.id;

    const fetchItems = useCallback(async () => {
        setLoading(true);
        setError(null);
        try {
            const response = await ItemService.getItemsByBrandId(brandID, searchTerm, pageNumber, pageSize);
            setTotalElements(response.data.totalElements);
            setFilteredItems(response.data.content);
            setLoading(false);
        } catch (error) {
            setError(error.response ? `Server responded with an error: ${error.response.statusText}` : "No response from server.");
            setLoading(false);
        }
    }, [brandID, searchTerm, pageNumber, pageSize]);

    useEffect(() => {
        fetchItems();
    }, [fetchItems]);

    const handleSearch = () => {
        setPageNumber(0);
        fetchItems();
    };

    const handleKeyDown = (event) => {
        if (event.key === 'Enter') {
            handleSearch();
        }
    };

    const handleChangePage = (newPage) => {
        if (newPage >= 0 && newPage < pageCount) {
            setPageNumber(newPage);
        }
    };

    const handleChangeRowsPerPage = (event) => {
        setPageSize(+event.target.value);
        setPageNumber(0);
    };

    const pageCount = totalElements > 0 ? Math.ceil(totalElements / pageSize) : 1;

    return (
        <div className="event-page">
            <div className="mb-4">
                <div className='d-flex align-items-center justify-content-between'>
                    <InputGroup className="search-bar">
                        <FormControl
                            placeholder="Search items by event name..."
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
                        <FormControl as="select" value={pageSize} onChange={handleChangeRowsPerPage}>
                            <option value={24}>24</option>
                            <option value={48}>48</option>
                            <option value={96}>96</option>
                        </FormControl>
                    </div>
                </div>

            </div>
            {loading && <Spinner animation="border" />}
            {error && <div className="error-message">{error}</div>}
            <h2 className="text-center">Items List</h2>

            <div className="event-list">
                {filteredItems.length > 0 ? (
                    filteredItems.map(item => (
                        <ItemCard key={item.id} item={item} />
                    ))
                ) : (
                    <div>No items found.</div>
                )}
            </div>
            <div className="pagination mt-4">
                <Pagination>
                    <Pagination.Prev onClick={() => handleChangePage(pageNumber > 0 ? pageNumber - 1 : 0)} />
                    {[...Array(pageCount).keys()].map(number => (
                        <Pagination.Item
                            key={number}
                            active={number === pageNumber}
                            onClick={() => handleChangePage(number)}
                        >
                            {number + 1}
                        </Pagination.Item>
                    ))}
                    <Pagination.Next onClick={() => handleChangePage(pageNumber < pageCount - 1 ? pageNumber + 1 : pageCount - 1)} />
                </Pagination>
            </div>
        </div>
    );
};

export default ItemList;
