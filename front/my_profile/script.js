let originalUserData = null; // Переменная для хранения данных пользователя перед редактированием

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

document.addEventListener("DOMContentLoaded", () => {
  fetchUserData();

  // Добавляем обработчик для кнопки редактирования после загрузки данных
  document.querySelector(".edit-button").addEventListener("click", enableEditMode);
});

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
    originalUserData = user;

    document.getElementById("user-id").textContent = user.userId;
    document.getElementById("name").textContent = user.name;
    document.getElementById("surname").textContent = user.surname;
    document.getElementById("dob").textContent = user.dateOfBirth;
    document.getElementById("login").textContent = user.login;
    document.getElementById("phone").textContent = user.telephoneNumber;
    document.getElementById("card-number").textContent = user.cardNumber;
    document.getElementById("card-expiry").textContent = user.cardExpDate;
    document.getElementById("document-number").textContent = user.documentNumber;

    // Привязываем обработчик к кнопке "Редактировать" (если её нет, создаём её)
    let editButton = document.querySelector(".edit-button");
    if (!editButton) {
        editButton = document.createElement("button");
        editButton.classList.add("edit-button");
        editButton.textContent = "Edit";
        editButton.addEventListener("click", enableEditMode);

        document.querySelector(".profile-info").appendChild(editButton);
    }
}

function enableEditMode() {
  const profileInfo = document.querySelector(".profile-info");

  if (!originalUserData) {
      console.error("No original user data found.");
      return;
  }

  profileInfo.innerHTML = `
      <h2>Edit Personal Information</h2>
      <p><strong>User ID:</strong> <span>${originalUserData.userId}</span></p>
      <p><strong>Login:</strong> <span>${originalUserData.login}</span></p>
      <p><strong>Name:</strong> <input type="text" id="edit-name" value="${originalUserData.name || ""}" required></p>
      <p><strong>Surname:</strong> <input type="text" id="edit-surname" value="${originalUserData.surname || ""}"></p>
      <p><strong>Date of Birth:</strong> 
          <input type="date" id="edit-dob" value="${originalUserData.dateOfBirth || ""}">
      </p>
      <p><strong>Phone:</strong> <input type="text" id="edit-phone" value="${originalUserData.telephoneNumber || ""}"></p>
      <p><strong>Card Number:</strong> <input type="text" id="edit-card-number" value="${originalUserData.cardNumber || ""}"></p>
      <p><strong>Card Expiry:</strong> <input type="text" id="edit-card-expiry" value="${originalUserData.cardExpDate || ""}"></p>
      <p><strong>Document Number:</strong> <input type="text" id="edit-document-number" value="${originalUserData.documentNumber || ""}"></p>
      <button class="save-button">Save Changes</button>
      <button class="cancel-button">Cancel</button>
      <p id="error-message" style="color: red; display: none;">Name cannot be empty.</p>
  `;

  document.querySelector(".save-button").addEventListener("click", validateAndSave);
  document.querySelector(".cancel-button").addEventListener("click", restoreProfileInfo);
}

// === Проверка перед сохранением ===
function validateAndSave() {
    const nameInput = document.getElementById("edit-name").value.trim();
    const errorMessage = document.getElementById("error-message");

    if (!nameInput) {
        errorMessage.style.display = "block"; // Показываем ошибку
        return;
    }

    saveChanges(); // Если имя не пустое, сохраняем данные
}

// === Сохранение изменений ===
async function saveChanges() {
  const authToken = localStorage.getItem("authToken");

  const updatedData = {
      userId: originalUserData.userId,
      login: originalUserData.login,
      password: originalUserData.password,
      reservations: originalUserData.reservations,
      name: document.getElementById("edit-name").value.trim() || null,
      surname: document.getElementById("edit-surname").value.trim() || null,
      dateOfBirth: document.getElementById("edit-dob").value || null,
      telephoneNumber: document.getElementById("edit-phone").value.trim() || null,
      cardNumber: document.getElementById("edit-card-number").value.trim() || null,
      cardExpDate: document.getElementById("edit-card-expiry").value.trim() || null,
      documentNumber: document.getElementById("edit-document-number").value.trim() || null,
  };

  console.log(updatedData);

  try {
      const response = await fetch("http://localhost:8080/api/user/logged", {
          method: "PUT",
          headers: {
              "Authorization": `Bearer ${authToken}`,
              "Content-Type": "application/json"
          },
          body: JSON.stringify(updatedData)
      });

      if (!response.ok) {
          throw new Error(`Error - PUT request: ${response.status}`);
      }

      alert("Profile updated successfully!");
      restoreProfileInfo();
  } catch (err) {
      console.error("Error updating user data:", err);
      alert("Failed to update profile. Try again later.");
  }
}

// === Восстановление исходных данных ===
function restoreProfileInfo() {
  window.location.href = "my_profile.html";
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
