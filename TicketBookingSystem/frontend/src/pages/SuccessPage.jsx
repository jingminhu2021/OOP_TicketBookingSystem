import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import axios from "axios";
import { Button } from "react-bootstrap";

function SuccessPage(){
  const token = localStorage.getItem('token');
  const [userData, setUserData] = useState(null);
  const config = {
      headers: { Authorization: `Bearer ${token}` }
  };

  const session_id = useParams().session_id;
  console.log(session_id);
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

  useEffect(()=>{
    const setBookingHistory = async () =>{
      try{
        let api_endpoint_url = 'http://localhost:8080/transaction/success';
        let data ={
          session_id: session_id
        }
        console.log("here is the passed data:",data);
        console.log("here is the user's identity:",config);
        const response = await axios.post(api_endpoint_url,data,config);
        console.log(response.data);
      }catch(error){
        console.log("Error setting transaction to history:",error);
      };
    };
    setBookingHistory();
  },[])
    return(
      <div className="container">
        {/* <section className="py-5 bg-light mb-3">
            <div className="container">
                <div className="row px-4 px-lg-5 py-lg-4 align-items-center">
                    <div className="col-lg-6">
                        <h1 className="h2 text-uppercase mb-0">payment success</h1>
                    </div>
                    <div className="col-lg-6 text-lg-end">
                        <nav aria-label="breadcrumb">
                            <ol className="breadcrumb justify-content-lg-end mb-0 px-0 bg-light">
                                <li className="breadcrumb-item"><a className="text-dark" href="/">Back to Home</a></li>
                            </ol>
                        </nav>
                    </div>
                </div>
            </div>
        </section> */}
        <section className="">
          <div className="mb-5">
            
          </div>
          <div className="container">
            
            <div className="row mb-5">
              {/* <div class="col-lg-6 mt-4">
                <div class="row m-sm-0">
                  <div class="col-sm-10 w-100">
                    <div class="swiper product-slider w-100">
                      <div class="swiper-wrapper">
                        <div><a class="glightbox product-view" data-gallery="gallery2" data-glightbox="Product item 1"><img class="img-fluid w-100" src="/PaymentSuccess.jpg" alt="..."/></a></div>
                      </div>
                    </div>
                  </div>
                </div>
              </div> */}
            
              <div className="col-lg-12">
                <ul className="nav nav-tabs border-0" id="myTab" role="tablist">
                  <li className="nav-item"><a className="nav-link text-uppercase active" id="description-tab" data-bs-toggle="tab" href="#description" role="tab" aria-controls="description" aria-selected="true">Thank you for your purchase</a></li>
                </ul>
                <div className="tab-content mb-5" id="myTabContent">
                  <div className="tab-pane fade show active" id="description" role="tabpanel" aria-labelledby="description-tab">
                    <div className="p-4 p-lg-5 bg-light">
                      <h6 className="text-uppercase">✅ Congratulation!</h6>
                      <p className="text-muted text-sm mb-0">You have successfully made your payment. You may now view your booking in your booking history!</p>
                    </div>
                  </div>
                </div>
                
                <div className="container p-0">
                    <div className="row">
                        <div className="col">
                          {/* <Button href="/booking-history">View Booking History</Button> */}
                        </div>
                        <div className="col-auto ml-auto">
                            
                        {userData && userData.role !== 'Ticketing_Officer' && (
                            <Button variant="outline-primary" href="/booking-history">View Booking History</Button>
                        )}
                            <span className="p-2"></span>
                            <Button href="/">Back to Home</Button>
                        </div>
                    </div>
                </div>
                    
            </div>
            
              
            </div>
          </div>
        </section>
      </div>

    )
}
export default SuccessPage