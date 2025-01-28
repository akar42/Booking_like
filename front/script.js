const navBar = document.querySelector(".navbar");
let prevScrollPos = window.scrollY;

window.addEventListener("scroll", function () {
  let currScrollPos = window.scrollY;

  if (currScrollPos > prevScrollPos) {
    navBar.style.transform = `translateY(-105%)`;
  } else {
    navBar.style.transform = `translateY(0%)`;
  }

  prevScrollPos = currScrollPos;
});


const reservations = [
  {
    destination: "New York",
    checkIn: "2025-02-15",
    checkOut: "2025-02-20",
    guests: 4,
    roomId: "123",
    totalCost: "$500",
  },
  {
    destination: "Los Angeles",
    checkIn: "2025-02-10",
    checkOut: "2025-02-15",
    guests: 2,
    roomId: "124",
    totalCost: "$300",
  },
  {
    destination: "New York",
    checkIn: "2025-03-05",
    checkOut: "2025-03-10",
    guests: 5,
    roomId: "125",
    totalCost: "$400",
  },
  {
    destination: "New York",
    checkIn: "2025-03-05",
    checkOut: "2025-03-10",
    guests: 3,
    roomId: "125",
    totalCost: "$500",
  },
];

document.getElementById("find-hotel-form").addEventListener("submit", function (event) {
  event.preventDefault();

  const findReservationsSection = document.getElementById("find-reservations");
  findReservationsSection.classList.add("show");
});

document.getElementById("find-hotel-form").addEventListener("submit", function(event){
  event.preventDefault();

  const destination = document.getElementById("destination").value.trim().toLowerCase();
  const people = parseInt(document.getElementById("people").value, 10);
  const checkin = new Date(document.getElementById("checkin").value);
  const checkout = new Date(document.getElementById("checkout").value);
  
  const filteredReservations = reservations.filter((reservation) => {
    const reservationCheckIn = new Date(reservation.checkIn);
    const reservationCheckOut = new Date(reservation.checkOut);

    return (
      reservation.destination.toLowerCase().includes(destination) &&
      reservation.guests >= people &&
      checkin >= reservationCheckIn &&
      checkout <= reservationCheckOut
    );
  });

    const reservationsList = document.getElementById("reservations-list");
    reservationsList.innerHTML = "";

    if (filteredReservations.length > 0) {
      filteredReservations.forEach((reservation) => {
        const reservationElement = document.createElement("div");
        reservationElement.classList.add("reservation-item");
  
        reservationElement.innerHTML = `
          <div>
            <p><strong>Destination:</strong> ${reservation.destination}</p>
            <p><strong>Check-in:</strong> ${reservation.checkIn}</p>
            <p><strong>Check-out:</strong> ${reservation.checkOut}</p>
            <p><strong>Guests:</strong> ${reservation.guests}</p>
            <p><strong>Total Cost:</strong> ${reservation.totalCost}</p>
          </div>
          <div class = "choose-reservation">
            <button class="reserve-btn" id ="reserve-it-btn">Reserve it</button>
            <div class="stars"></div>
          </div>
        `;
  
        reservationsList.appendChild(reservationElement);
      });
    } else {
      reservationsList.innerHTML = "<p>No matching reservations found.</p>";
    }

    document.getElementById("find-reservations").style.display = "block";

    const findReservationsSection = document.getElementById("find-reservations");
    findReservationsSection.classList.add("show");

    setTimeout(() => {
      findReservationsSection.scrollIntoView({ behavior: "smooth" });
    }, 110);
});

function goToSearchReservations() {
  const findReservationsSection = document.getElementById("find-hotel-form");

  setTimeout(() => {
    findReservationsSection.scrollIntoView({ behavior: "smooth" });
  }, 110);
}



function setCookie(name, value, days) {
  const date = new Date();
  date.setTime(date.getTime() + days * 24 * 60 * 60 * 1000);
  document.cookie = `${name}=${value}; expires=${date.toUTCString()}; path=/`;
}

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

function deleteCookie(name) {
  document.cookie = `${name}=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/`;
}

function updateAuthButton() {
  const authLink = document.getElementById("auth-link");
  const isLoggedIn = getCookie("isLoggedIn");
  const userRole = getCookie("adminRole") || getCookie("userRole");

  if (isLoggedIn) {
    authLink.textContent = "Log Out";
    authLink.setAttribute("href", "#");
    authLink.classList.remove("log-in");
    authLink.classList.add("log-out");

    authLink.addEventListener("click", () => {
      deleteCookie("isLoggedIn");
      deleteCookie("adminRole");
      deleteCookie("userRole");
      updateAuthButton();
    });
  } else {
    authLink.textContent = "Log In";
    authLink.setAttribute("href", "login.html");
    authLink.classList.remove("log-out");
    authLink.classList.add("log-in");
  }
}

document.addEventListener("DOMContentLoaded", updateAuthButton);

window.addEventListener("scroll", function () {
  let currScrollPos = window.scrollY;

  if (currScrollPos > prevScrollPos) {
    navBar1.style.transform = `translateY(-105%)`;
  } else {
    navBar1.style.transform = `translateY(0%)`;
  }

  prevScrollPos = currScrollPos;
});

function goToPersonalPage(){
  const userRole = getCookie("userRole");
  const adminRole = getCookie("adminRole");

  if (adminRole === "admin") {
    window.location.href = "admin_active_reservations.html";
  } else if (userRole === "user") {
    window.location.href = "my-profile.html";
  } else {
    alert("You must log in to access this page.");
    window.location.href = "login.html";
  }
}
document.getElementById("my-profile-button").addEventListener("click", goToPersonalPage);

function getCurrentUserId(){
  return getCookie("userId");
}

function checkLogin(){
  const currentUserId = getCurrentUserId();
  return currentUserId != null;
}

function acceptReservation(){
  const acceptButton = document.getElementsById("reserve-it-btn");
  acceptButton.addEventListener("click", (event) => {
    const checkUser = checkLogin();
    if(checkUser){
      
    }else{
      alert("Please log in to proces reservation");
    }
  })
}
