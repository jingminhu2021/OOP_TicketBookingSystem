import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Modal, Button, Form, InputGroup, Alert } from 'react-bootstrap';

function UpdateTicketType(props){
    const ticket_id = props.ticketId;
    const token = localStorage.getItem('token');
    const config = {
        headers: { Authorization: `Bearer ${token}` }
    };

    const [show, setShow] = useState(false);
    const [show2, setShow2] = useState(false);
    const [userData, setUserData] = useState(null);
    const [results, setResults] = useState({
        message: 'Loading...'
    });

    const [ticketType, setTicketType] = useState({
        category: '',
        price: '',
        number_of_ticket: '',
        cancellation_fee: '',
        event_id: ''
    });

    const handleShow = () => {
        setShow(true);
        getTicketData();
    }
    const handleClose = () => setShow(false);

    const handleShow2 = () => setShow2(true);
    const handleClose2 = () => {
        setResults({message: 'Loading...'});
        setShow2(false);
    }
    const [showCategoryAlert, setShowCategoryAlert] = useState(false);
    const [showPriceAlert, setShowPriceAlert] = useState(false);
    const [showNumberOfTicketAlert, setShowNumberOfTicketAlert] = useState(false);
    const [showCancellationFeeAlert, setShowCancellationFeeAlert] = useState(false);

    const handleInputChange = (e) => {
        const { name, value } = e.target;

        if (name == 'price'){
            setShowPriceAlert(false);

            if (value < 0) {
                setTicketType({
                    ...ticketType,
                    [name]: 0,
                });
                
                setShowPriceAlert(true);
            }
        }

        if (name == 'number_of_ticket'){
            setShowNumberOfTicketAlert(false);
            if (value < 0) {
                setTicketType({
                    ...ticketType,
                    [name]: 0,
                });
            
                setShowNumberOfTicketAlert(true);
            }
        }

        if (name == 'cancellation_fee'){
            setShowCancellationFeeAlert(false);
            if (value < 0) {
                setTicketType({
                    ...ticketType,
                    [name]: 0,
                });
                setShowCancellationFeeAlert(true);
            } else if (value > 100) {
                setTicketType({
                    ...ticketType,
                    [name]: 100,
                });
                setShowCancellationFeeAlert(true);
            }
        }
    
        setTicketType({
            ...ticketType,
            [name]: value,
        });
    };

    const getTicketData = () =>{
        let api_endpoint_url = 'http://localhost:8080/ticketType/viewSingleTicketType';
        const bodyParameters = {
            ticketTypeId: ticket_id
        };
        
        axios.post(api_endpoint_url, bodyParameters, config)
        .then(function (response) {
            var data = response.data;
            console.log(data);  
            setTicketType({
                category: data.eventCat,
                price: data.eventPrice,
                number_of_ticket: data.numberOfTix,
                cancellation_fee: data.cancellationFeePercentage,
                event_id: data.eventId
            }); 
        })
        .catch(function (error) {
            console.error('Error occurred:', error);
        });
        
    }

    const handleSubmit = (e) => {
        e.preventDefault();
        console.log('Form data submitted:', ticketType);
        // Add your authentication logic here, e.g., send data to an API
        send_onsubmit(ticket_id, ticketType.category, ticketType.price, ticketType.number_of_ticket, ticketType.cancellation_fee, ticketType.event_id);
        handleClose();
        handleShow2();
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


    const send_onsubmit = (ticket_id, category, price, cancellation_fee, number_of_ticket, event_id) => {
        let api_endpoint_url = 'http://localhost:8080/ticketType/updateTicketType';

        var bodyParameters = {
            eventId: event_id,
            ticketTypeId: ticket_id,
            eventCat: category,
            eventPrice: price,
            cancellationFeePercentage: cancellation_fee,
            numberOfTix: number_of_ticket
        };

        axios.post(api_endpoint_url, bodyParameters, config)
        .then((response) => {
            console.log(response);
            setResults(response.data);
        })
        .catch((error) => {
            console.error('Error occurred:', error);
        });
        
    }

    return(
        <>
        {userData && userData.role === 'Event_Manager' &&(
            <Button variant="primary" onClick={handleShow}>
                <i className="fas fa-plus me-1 text-gray fw-normal"></i> Edit
            </Button>
        )}

        <Modal show={show} onHide={handleClose}>
            <Modal.Header closeButton>
                <Modal.Title>Update Ticket Details</Modal.Title>
            </Modal.Header>
        
            <Modal.Body>
                <Form onSubmit={handleSubmit}> 
                    <Form.Group className="mb-3" controlId="formBasic">
                                
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
                            <Form.Control type="number" placeholder="Enter price" name="price" value={ticketType.price} onChange={handleInputChange} required/>

                        </InputGroup>
                        {showPriceAlert && 
                            <Alert variant="danger" onClose={() => setShowPriceAlert(false)} dismissible>
                                <p>
                                    Price is required.
                                </p>
                            </Alert>
                        }

                                
                        <Form.Label>Number of ticket:</Form.Label>
                        <Form.Control type="number" placeholder="Enter number of ticket" name="number_of_ticket" value={ticketType.number_of_ticket} onChange={handleInputChange} required/>
                        {showNumberOfTicketAlert && 
                            <Alert variant="danger" onClose={() => setShowNumberOfTicketAlert(false)} dismissible>
                                <p>
                                    Minimum 0 ticket.
                                </p>
                            </Alert>
                        }
                                

                        <Form.Label> Cancellation fee (Percentage)</Form.Label>

                        <InputGroup>
                            <Form.Control type="number" placeholder="Enter cancellation fee" name="cancellation_fee" value={ticketType.cancellation_fee} onChange={handleInputChange} required/>
                            <InputGroup.Text id="basic-addon1">%</InputGroup.Text>
                        </InputGroup>

                        {showCancellationFeeAlert && 
                            <Alert variant="danger" onClose={() => setShowCancellationFeeAlert(false)} dismissible>
                                <p>
                                    Acceptable range is between 0 to 100%.
                                </p>
                            </Alert>
                        }

                    </Form.Group>
                    <Button variant='primary' type='submit'>Update Ticket</Button>
                </Form>
            </Modal.Body>
        </Modal>

        <Modal show={show2} onHide={handleClose2}>
            <Modal.Header closeButton>
                <Modal.Title>Update Ticket Result</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <p>{results.message}</p>
            </Modal.Body>
        </Modal>
        </>
    )


}
export default UpdateTicketType;