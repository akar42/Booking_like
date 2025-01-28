const reservations = [
    {
      reservationId: "TYH123",
      checkIn: "January 15, 2025",
      checkOut: "January 20, 2025",
      guests: 4,
      status: "Confirmed",
      roomId: "123",
      totalCost: "$500",
      startDate: "2025-01-15",
      endDate: "2025-01-20",
      userId: "12345"
    },
    {
        reservationId: "TYH123",
        checkIn: "January 20, 2025",
        checkOut: "January 20, 2025",
        guests: 4,
        status: "Confirmed",
        roomId: "123",
        totalCost: "$500",
        startDate: "2025-01-20",
        endDate: "2025-01-25",
        userId: "123"
      },
      {
        reservationId: "TYH123",
        checkIn: "January 15, 2025",
        checkOut: "January 20, 2025",
        guests: 4,
        status: "Confirmed",
        roomId: "123",
        totalCost: "$500",
        startDate: "2025-01-15",
        endDate: "2025-01-20",
        userId: "12345"
      },
    // ... other reservations
  ];


const personalInfo = [
{
  userId: "12345",
  name: "John",
  surname: "Doe",
  dateOfBirth: "1990-01-01",
  login: "johndoe",
  phone: "+123456789",
  cardNumber: "1234-5678-9012-3456",
  cardExpiry: "12/25",
  documentNumber: "ABC123456"
},
{
  userId: "123",
  name: "pididi",
  surname: "pididid",
  dateOfBirth: "1990-01-01",
  login: "pididi",
  phone: "+123456789",
  cardNumber: "1234-5678-9012-3456",
  cardExpiry: "12/25",
  documentNumber: "ABC123456"
},
] 


function getCookie(name) {
  const cookies = document.cookie.split("; ");
  for (const cookie of cookies) {
    const [key, value] = cookie.split("=");
    if (key === name) {
      return value;
    }
  }
  return null;
}

function getCurrentUserId(){
  return getCookie("userId");
}

function getCurrentUser(){
  const currentUserId = getCurrentUserId();
  return personalInfo.find((user) => user.userId === currentUserId);
}

function filterUserId(){
  const currentUserId = getCurrentUserId();
  if (!currentUserId) {
    alert("You must log in to see your reservations.");
    return [];
  }

  return reservations.filter((reservation) => reservation.userId === currentUserId);
}


function populateReservations(reservations) {
    const form = document.getElementById("reservations-form"); 
    form.innerHTML = ""; 

    const filteredReservations = filterUserId();
    
    filteredReservations.forEach((reservation) => {
      const reservationElement = document.createElement("div");
      reservationElement.classList.add("active-reservation");
  
      reservationElement.innerHTML = `
        <div class="active-reservation-info">
          <label for="reservation" class="reservation-label">
            <strong>Reservation Id:</strong> <span>${reservation.reservationId}</span>
          </label>
          <p><strong>Check-in:</strong> <span>${reservation.checkIn}</span></p>
          <p><strong>Check-out:</strong> <span>${reservation.checkOut}</span></p>
          <p><strong>Guests:</strong> <span>${reservation.guests}</span></p>
          <p><strong>Status:</strong> <span>${reservation.status}</span></p>
        </div>
        <div class="active-reservation-button">
          <a class="button-to-kill">Creeps or bluds?</a>
        </div>
      `;
  
      const button = reservationElement.querySelector(".button-to-kill");
      button.addEventListener("click", () => {
        storeReservationData(reservation);
        window.location.href = "reservation.html";
      });
  
      form.appendChild(reservationElement);
    });

  }     

  function storeReservationData(reservation) {
    const currentUser = getCurrentUser();
    const combinedData = {
        reservation: reservation,
        user: currentUser,
    };
  
    
    document.cookie = `selectedReservation=${encodeURIComponent(
        JSON.stringify(combinedData)
      )}; path=/`;
    
  }

function populatePersonalInfo() {
    const currentUser = getCurrentUser();
    document.getElementById("user-id").textContent = currentUser.userId;
    document.getElementById("name").textContent = currentUser.name;
    document.getElementById("surname").textContent = currentUser.surname;
    document.getElementById("dob").textContent = currentUser.dateOfBirth;
    document.getElementById("login").textContent = currentUser.login;
    document.getElementById("phone").textContent = currentUser.phone;
    document.getElementById("card-number").textContent = currentUser.cardNumber;
    document.getElementById("card-expiry").textContent = currentUser.cardExpiry;
    document.getElementById("document-number").textContent = currentUser.documentNumber;
}


function checkUserAccess() {
  const isLoggedIn = getCookie("isLoggedIn");
  const userRole = getCookie("userRole");
}
checkUserAccess();

document.addEventListener("DOMContentLoaded", () => {
  populateReservations();
  populatePersonalInfo();
});