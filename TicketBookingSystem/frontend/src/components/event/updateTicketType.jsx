import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Modal, Button, Form, InputGroup, Alert } from 'react-bootstrap';

function UpdateTicketType(ticket_id){
    const token = localStorage.getItem('token');
    const config = {
        headers: { Authorization: `Bearer ${token}` }
    };

    const [show, setShow] = useState(false);
    const [userData, setUserData] = useState(null);
    const [ticketType, setTicketType] = useState({
        category: '',
        price: '',
        number_of_ticket: '',
        cancellation_fee: ''
    });
    const handleShow = () => setShow(true);
    const handleClose = () => setShow(false);

    const [showCategoryAlert, setShowCategoryAlert] = useState(false);
    const [showPriceAlert, setShowPriceAlert] = useState(false);
    const [showNumberOfTicketAlert, setShowNumberOfTicketAlert] = useState(false);
    const [showCancellationFeeAlert, setShowCancellationFeeAlert] = useState(false);

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        if (!value) {
            switch (name) {
                case 'category':
                    setShowCategoryAlert(true);
                    break;
                case 'price':
                    setShowPriceAlert(true);
                    break;
                case 'number_of_ticket':
                    setShowNumberOfTicketAlert(true);
                    break;
                case 'cancellation_fee':
                    setShowCancellationFeeAlert(true);
                    break;
                default:
                    break;
            }
        }
        if (name === 'cancellation_fee') {
            if (value < 0) {
                value = 0;
            } else if (value > 100) {
                value = 100;
            }
        }

        setTicketType({
            ...ticketType,
            [name]: value,
        });
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        console.log('Form data submitted:', ticketType);
        // Add your authentication logic here, e.g., send data to an API
        send_onsubmit(ticket_id, ticketType.category, ticketType.price, ticketType.number_of_ticket, ticketType.cancellation_fee);
    };

    useEffect(() => {
        
        const getUserData = async (token) => {
            let api_endpoint_url = 'http://localhost:8080/user/getLoggedInUser';
            
            const bodyParameters = {};
            try {
                const response = await axios.post(api_endpoint_url, bodyParameters, config);
                setUserData(response.data);
            } catch (error) {
                console.error('Error occurred:', error);
            }
        };

        if (token) {
            getUserData(token);
        }
        
    }, []); // Empty dependency array ensures this effect runs only once


    const send_onsubmit = (ticket_id, category, price, cancellation_fee, number_of_ticket) => {
        let api_endpoint_url = 'http://localhost:8080/ticketType/updateTicketType';

        var bodyParameters = {
            ticketTypeId: ticket_id,
            eventCat: category,
            eventPrice: price,
            cancellationFeePercentage: cancellation_fee,
            numberOfTix: number_of_ticket
        };

        axios.post(api_endpoint_url, bodyParameters, config)
        .then((response) => {
            console.log(response);
            setResults(prevResults => [...prevResults, response]);
        })
        .catch((error) => {
            console.error('Error occurred:', error);
        });
        
    }

    return(
        <>
        {userData && userData.role === 'Event_Manager' &&(
            <Button variant="danger" onClick={handleShow}>
                <i className="fas fa-plus me-1 text-gray fw-normal"></i> Update
            </Button>
        )}

        <Modal show={show} onHide={handleClose}>
            <Modal.Header closeButton>
                <Modal.Title>Update Ticket Details</Modal.Title>
            </Modal.Header>
        </Modal>
        <Modal.Body>
            <Form onSubmit={handleSubmit}> 
                <Form.Group className="mb-3" controlId="formBasic">
                    <h5>Add new Ticket {index+1}:</h5>
                            
                    <Form.Label>Event Category:</Form.Label>
                    <Form.Control type="text" placeholder="Enter category name" name="category" value={ticketType.category || ''} onChange={handleInputChange} required/>
                    {showCategoryAlert && 
                        <Alert variant="danger" onClose={() => setShowCategoryAlert(false)} dismissible>
                            <p>
                                Category is required.
                            </p>
                        </Alert>
                    }

                    <Form.Label>Event Price:</Form.Label>
                    <InputGroup>
                        <InputGroup.Text id="basic-addon1">SGD$</InputGroup.Text>
                        <Form.Control type="number" placeholder="Enter price" name="price" value={ticketType.price  || ''} onChange={handleInputChange} required/>

                    </InputGroup>
                    {showPriceAlert && 
                        <Alert variant="danger" onClose={() => setShowPriceAlert(false)} dismissible>
                            <p>
                                Price is required.
                            </p>
                        </Alert>
                    }

                            
                    <Form.Label>Number of ticket:</Form.Label>
                    <Form.Control type="number" placeholder="Enter number of ticket" name="number_of_ticket" value={ticketType.number_of_ticket  || ''} onChange={handleInputChange} required/>
                    {showNumberOfTicketAlert && 
                        <Alert variant="danger" onClose={() => setShowNumberOfTicketAlert(false)} dismissible>
                            <p>
                                Number of ticket is required.
                            </p>
                        </Alert>
                    }
                            

                    <Form.Label> Cancellation fee (Percentage)</Form.Label>

                    <InputGroup>
                        <Form.Control type="number" min="0" max="100" placeholder="Enter cancellation fee" name="cancellation_fee" value={ticketType.cancellation_fee  || ''} onChange={handleInputChange} required/>
                        <InputGroup.Text id="basic-addon1">%</InputGroup.Text>
                    </InputGroup>

                    {showCancellationFeeAlert && 
                        <Alert variant="danger" onClose={() => setShowCancellationFeeAlert(false)} dismissible>
                            <p>
                                Cancellation fee is required.
                            </p>
                        </Alert>
                    }

                </Form.Group>
            </Form>
        </Modal.Body>
        </>
    )


}
export default UpdateTicketType;