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

document.addEventListener("DOMContentLoaded", fetchUserData);

async function fetchUserData() {
    const authToken = localStorage.getItem("authToken");
    if (!authToken) {
        alert("You must log in to access this page.");
        window.location.href = "login.html";
        return;
    }

    try {
        const response = await fetch("http://localhost:8080/api/user/logged", {
            method: "GET",
            headers: {
                "Authorization": `Bearer ${authToken}`,
                "Content-Type": "application/json"
            }
        });

        if (!response.ok) {
            throw new Error(`Error - GET request: ${response.status}`);
        }

        const userData = await response.json();
        populateUserData(userData);
        populateReservations(userData.reservations);
    } catch (err) {
        console.error("Error fetching user data:", err);
    }
}

// === Заполнение личных данных ===
function populateUserData(user) {
    document.getElementById("user-id").textContent = user.userId;
    document.getElementById("name").textContent = user.name;
    document.getElementById("surname").textContent = user.surname;
    document.getElementById("dob").textContent = user.dateOfBirth;
    document.getElementById("login").textContent = user.login;
    document.getElementById("phone").textContent = user.telephoneNumber;
    document.getElementById("card-number").textContent = user.cardNumber;
    document.getElementById("card-expiry").textContent = user.cardExpDate;
    document.getElementById("document-number").textContent = user.documentNumber;
}

// === Отображение бронирований ===
function populateReservations(reservations) {
    const form = document.getElementById("reservations-form");
    form.innerHTML = "";

    if (!reservations || reservations.length === 0) {
        form.innerHTML = "<p>No active reservations found.</p>";
        return;
    }

    reservations.forEach(reservation => {
        const reservationElement = document.createElement("div");
        reservationElement.classList.add("active-reservation");

        reservationElement.innerHTML = `
            <div class="active-reservation-info">
                <label class="reservation-label">
                    <strong>Reservation Id:</strong> <span>${reservation.reservationId}</span>
                </label>
                <p><strong>Check-in:</strong> <span>${reservation.startDate}</span></p>
                <p><strong>Check-out:</strong> <span>${reservation.endDate}</span></p>
                <p><strong>Total Cost:</strong> <span>${reservation.totalCost} $</span></p>
                <p><strong>Status:</strong> <span>${reservation.status}</span></p>
            </div>
            <div class="active-reservation-button">
                <a class="btn-s button-to-details">View Details</a>
            </div>
        `;

        const button = reservationElement.querySelector(".button-to-details");
        button.addEventListener("click", () => {
            storeReservationData(reservation);
            window.location.href = "reservation.html";
        });

        form.appendChild(reservationElement);
    });
}

// === Сохранение бронирования в cookie ===
function storeReservationData(reservation) {
  if (!reservation) {
      console.error("Attempted to store empty reservation.");
      return;
  }

  // Добавим логирование, чтобы проверить передаваемые данные
  console.log("Storing reservation:", reservation);

  document.cookie = `selectedReservation=${encodeURIComponent(
      JSON.stringify(reservation)
  )}; path=/`;

  window.location.href = "reservation.html";
}
