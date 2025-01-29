// ========== Скрытие/показ navbar при скролле ==========
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

// ========== Переход к форме поиска (скролл) ==========
function goToSearchReservations() {
  const findReservationsSection = document.getElementById("find-hotel-form");
  setTimeout(() => {
    findReservationsSection.scrollIntoView({ behavior: "smooth" });
  }, 110);
}

// ========== Работа с куками (логин, роли) ==========
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

// ========== Повторный код со скрытием navbar (если нужно) ==========
window.addEventListener("scroll", function () {
  let currScrollPos = window.scrollY;
  const navBar1 = document.querySelector(".navbar");
  if (currScrollPos > prevScrollPos) {
    navBar1.style.transform = `translateY(-105%)`;
  } else {
    navBar1.style.transform = `translateY(0%)`;
  }
  prevScrollPos = currScrollPos;
});

// ========== Переход на личную страницу (в зависимости от роли) ==========
function goToPersonalPage() {
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
document
  .getElementById("my-profile-button")
  .addEventListener("click", goToPersonalPage);

function getCurrentUserId() {
  return getCookie("userId");
}

function checkLogin() {
  // const currentUserId = getCurrentUserId();
  // const jwt = getCookie()
  return getCookie("isLoggedIn");
}

// ========== Обработка формы поиска и запрос к серверу ==========
document
  .getElementById("find-hotel-form")
  .addEventListener("submit", async function (event) {
    event.preventDefault();

    // const destination = document.getElementById("destination").value.trim();
    const people = document.getElementById("people").value.trim();
    const checkinValue = document.getElementById("checkin").value;
    const checkoutValue = document.getElementById("checkout").value;

    const reservationsList = document.getElementById("reservations-list");
    reservationsList.innerHTML = "";

    // Логика заполнения дат:
    // 1. Если ОБА поля дат пустые => GET /api/rooms
    // 2. Если ОБА поля дат заполнены => POST /api/rooms/filter
    // 3. Если одно пустое, другое нет => сообщение об ошибке и выходим

    if (!checkinValue && !checkoutValue && !people) {
      // Оба поля пусты => GET
      try {
        const response = await fetch("http://localhost:8080/api/rooms");
        if (!response.ok) {
          throw new Error(`Ошибка при GET-запросе: ${response.status}`);
        }
        const rooms = await response.json();
        renderRooms(rooms);
      } catch (err) {
        console.error(err);
        reservationsList.innerHTML =
          "<p>Error while loading all rooms</p>";
      }
    } else if ((checkinValue && checkoutValue) || people) {
      // Оба поля заполнены => POST
      try {
        const response = await fetch("http://localhost:8080/api/rooms/filter", {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            requiredGuests: people,
            startDate: checkinValue,
            endDate: checkoutValue,
            // Если нужно также учитывать destination/people => добавьте:
            // destination: destination,
            // people: people,
          }),
        });

        if (!response.ok) {
          throw new Error(`Ошибка при POST-запросе: ${response.status}`);
        }
        const rooms = await response.json();
        renderRooms(rooms);
      } catch (err) {
        console.error(err);
        reservationsList.innerHTML =
          "<p>Error while loading filtered rooms</p>";
      }
    } else {
      // Одно поле заполнено, второе пустое => показываем ошибку
      alert("Please either fill in both date fields or leave them blank");
      return;
    }

    // Показать секцию и проскроллить
    const findReservationsSection = document.getElementById("find-reservations");
    findReservationsSection.style.display = "block";
    findReservationsSection.classList.add("show");

    setTimeout(() => {
      findReservationsSection.scrollIntoView({ behavior: "smooth" });
    }, 110);
  });

// ========== Функция отображения комнат на экране ==========
function renderRooms(roomsArray) {
  const reservationsList = document.getElementById("reservations-list");

  if (!roomsArray || roomsArray.length === 0) {
    reservationsList.innerHTML = "<p>No matching reservations found.</p>";
    return;
  }

  roomsArray.forEach((room) => {
    const reservationElement = document.createElement("div");
    reservationElement.classList.add("reservation-item");

    reservationElement.innerHTML = `
      <div>
        <p><strong>Room ID:</strong> ${room.roomId || "-"}</p>
        <p><strong>Room Number:</strong> ${room.roomNumber || "-"}</p>
        <p><strong>Type:</strong> ${room.roomType || "-"}</p>
        <p><strong>Price:</strong> ${room.price || "-"}</p>
        <p><strong>Guests:</strong> ${room.guestNumber || "-"}</p>
        <p><strong>Floor:</strong> ${room.floorNumber || "-"}</p>
        <p><strong>Facilities:</strong> ${room.facilities || "-"}</p>
      </div>
      <div class="choose-reservation">
        <button class="reserve-btn" id="reserve-it-btn">Reserve it</button>
        <div class="stars"></div>
      </div>
    `;

    // Кнопка "Reserve it"
    const reserveBtn = reservationElement.querySelector("#reserve-it-btn");
    reserveBtn.addEventListener("click", () => {
      const userIsLoggedIn = checkLogin();
      if (!userIsLoggedIn) {
        alert("Please log in to proceed with the reservation");
        window.location.href = "login.html";
      } else {
        
        // Логика бронирования
        console.log("Reserving room:", room);
      }
    });

    reservationsList.appendChild(reservationElement);
  });
}

function acceptReservation() {
  const acceptButton = document.getElementById("reserve-it-btn");
  if (!acceptButton) return;
  acceptButton.addEventListener("click", (event) => {
    const checkUser = checkLogin();
    if (!checkUser) {
      alert("Please log in to proceed with reservation");
    } else {
      console.log("Reservation accepted");
    }
  });
}
