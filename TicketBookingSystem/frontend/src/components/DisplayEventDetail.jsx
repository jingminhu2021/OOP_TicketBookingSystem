import { useState, useEffect } from "react";
import axios from 'axios';  
import BookTicket from "./bookTicket";

const DisplayEventDetail=({eventData})=>{
  const token = localStorage.getItem('token');
  const [status, setStatus] = useState(eventData.event.status);
  const [userData, setUserData] = useState(null);
  const config = {
      headers: { Authorization: `Bearer ${token}` }
  };

  useEffect(() => {
        const getUserData = async (token) => {
            let api_endpoint_url = 'http://localhost:8080/user/getLoggedInUser';
            const bodyParameters = {
                key: "value"
            };
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
    }, []); 



  useEffect(() => {
      function checkDate(){
        var today = new Date();
        
        var eventDate = new Date(eventData.event.dateTime[0], eventData.event.dateTime[1]-1, eventData.event.dateTime[2], eventData.event.dateTime[3], eventData.event.dateTime[4]);
        
        if (Math.abs(eventDate - today) <= 24*60*60*1000) {
          return 'today';
        }else if(today > eventDate){
          return false;
        } 
        return true;
      }
    
      const dateCheckResult = checkDate();

      if (status === "Active" && dateCheckResult === 'today' && userData && userData.role !== "Ticketing_Officer") {
        setStatus("No longer for purchase online, please make your purchase on-site.");
      }else if (status === "Active" && dateCheckResult === false) {
        setStatus("No longer for purchase");
      }else if (status === "Active" && dateCheckResult !== 'today' && userData && userData.role === "Ticketing_Officer") {
        setStatus("It is not within 24 hours. Customer can still purchase the ticket online.");
      }

    //   if(status === "Active" && dateCheckResult === false){
        
    //     setStatus("No longer for purchase");
      
    // } 

  }, [eventData, userData]);
  
  console.log(status);

  const[bookTicket, setOpen] = useState(false);

    function formatDate(dateTimeArray) {
        const [year, month, day] = dateTimeArray.slice(0, 3);
        return `${year}-${month.toString().padStart(2, '0')}-${day.toString().padStart(2, '0')}`;
    }
    
    function formatTime(dateTimeArray) {
        const [hour, minute] = dateTimeArray.slice(3, 5);
        if(hour>12){
            return `${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')} pm`;
        }else{
            return `${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')} am`;
        }
    }

    const open=()=>{
      setOpen(true);
    }
    return(
      <div className="container">
        <section className="py-5 bg-light mb-3">
            <div className="container">
                <div className="row px-4 px-lg-5 py-lg-4 align-items-center">
                    <div className="col-lg-6">
                        <h1 className="h2 text-uppercase mb-0">{eventData.event.eventName}</h1>
                    </div>
                    <div className="col-lg-6 text-lg-end">
                        <nav aria-label="breadcrumb">
                            <ol className="breadcrumb justify-content-lg-end mb-0 px-0 bg-light">
                                <li className="breadcrumb-item"><a className="text-dark" href="/">Home</a></li>
                                <li className="breadcrumb-item active">{eventData.event.eventName}</li>
                            </ol>
                        </nav>
                    </div>
                </div>
            </div>
        </section>
        <section class="py-4 bg-light">
        <div class="container">
          <div class="row mb-5">
            <div class="col-lg-6 mt-4">
              <div class="row m-sm-0">
                <div class="col-sm-10 w-100">
                  <div class="swiper product-slider w-100">
                    <div class="swiper-wrapper">
                      <div><a class="glightbox product-view" data-gallery="gallery2" data-glightbox="Product item 1"><img class="img-fluid w-100" src={eventData.event.image} alt="..."/></a></div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="col-lg-6">
              <ul class="list-inline m-0 text-sm">
                <li class="list-inline-item m-0"><i class="fas fa-star small text-warning"></i></li>
                <li class="list-inline-item m-0 1"><i class="fas fa-star small text-warning"></i></li>
                <li class="list-inline-item m-0 2"><i class="fas fa-star small text-warning"></i></li>
                <li class="list-inline-item m-0 3"><i class="fas fa-star small text-warning"></i></li>
                <li class="list-inline-item m-0 4"><i class="fas fa-star small text-warning"></i></li>
              </ul>
              <ul class="list-unstyled small d-inline-block w-100">
              <li class="px-3 py-2 mb-1 bg-white">
                  <strong class="text-uppercase">Event status: </strong>
                  <span class="ms-2 text-muted">
                      {status}
                  </span>
              </li>
                <li class="px-3 py-2 mb-1 bg-white text-muted"><strong class="text-uppercase text-dark">Event type:</strong><a class="reset-anchor ms-2">{eventData.event.eventType}</a></li>
                <li class="px-3 py-2 mb-1 bg-white text-muted"><strong class="text-uppercase text-dark">Event venue:</strong><a class="reset-anchor ms-2">{eventData.event.venue}</a></li>
                <li class="px-3 py-2 mb-1 bg-white text-muted"><strong class="text-uppercase text-dark">Event date:</strong><a class="reset-anchor ms-2">{formatDate(eventData.event.dateTime)}</a></li>
                <li class="px-3 py-2 mb-1 bg-white text-muted"><strong class="text-uppercase text-dark">Event time:</strong><a class="reset-anchor ms-2">{formatTime(eventData.event.dateTime)}</a></li>
              </ul>
              {status && status === 'Active' && <BookTicket eventData={eventData} />}
              {/* {status && status === 'Active' && BookTicket(eventData)} */}
            </div>
            
          </div>
          <ul class="nav nav-tabs border-0" id="myTab" role="tablist">
            <li class="nav-item"><a class="nav-link text-uppercase active" id="description-tab" data-bs-toggle="tab" href="#description" role="tab" aria-controls="description" aria-selected="true">Description</a></li>
          </ul>
          <div class="tab-content mb-5" id="myTabContent">
            <div class="tab-pane fade show active" id="description" role="tabpanel" aria-labelledby="description-tab">
              <div class="p-4 p-lg-5 bg-white">
                <h6 class="text-uppercase">Event description </h6>
                <p class="text-muted text-sm mb-0">{eventData.event.description}</p>
              </div>
            </div>
            
          </div>
        </div>
      </section>

      </div>
    )
}
export default DisplayEventDetail;