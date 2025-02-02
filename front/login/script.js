const container = document.getElementById('container');
const registerBtn = document.getElementById('register');
const loginBtn = document.getElementById('login');

registerBtn.addEventListener('click', () => {
    container.classList.add("active");
});

loginBtn.addEventListener('click', () => {
    container.classList.remove("active");
});


const adminData = [
    {
      login: "admin2@gmail.com",
      adminId: "admin2",
      password: "1234", 
      role: "admin",
    },
    {
      login: "admin1@gmail.com",
      adminId: "admin1",
      password: "1212",
      role: "admin",
    },
  ];

  const userData = [
    {
        login: "user1@gmail.com",
        userId: 12345,
        password: "user1",
        role: "user"
    },
    {
        login: "user2@gmail.com",
        userId: 123,
        password: "user2",
        role: "user"
    }
  ]

  function setCookie(name, value, days) {
    const date = new Date();
    date.setTime(date.getTime() + days * 24 * 60 * 60 * 1000);
    const expires = `expires=${date.toUTCString()}`;
    document.cookie = `${name}=${encodeURIComponent(value)}; ${expires}; path=/`;
  }

  function getCookie(name) {
    const decodedCookie = decodeURIComponent(document.cookie);
    const cookies = decodedCookie.split("; ");
    for (const cookie of cookies) {
      const [key, value] = cookie.split("=");
      if (key === name) {
        return value;
      }
    }
    return null;
  }

  async function handleLogin(event) {
    event.preventDefault();
    const emailInput = document.querySelector(".sign-in input[type='email']").value;
    const passwordInput = document.querySelector(".sign-in input[type='password']").value;

    try {
        const response = await fetch("http://localhost:8080/api/auth/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ login: emailInput, password: passwordInput })
        });

        if (!response.ok) {
            throw new Error(`Login failed: ${response.statusText}`);
        }

        const data = await response.json();
        const token = data.token;

        console.log("JWT Token:", token);

        // Декодируем JWT
        const decodedToken = parseJwt(token);

        console.log("Decoded Token:", decodedToken);

        // Сохраняем данные в localStorage
        localStorage.setItem("authToken", token);
        localStorage.setItem("userLogin", decodedToken.sub);
        localStorage.setItem("userRole", decodedToken.role);

        // Перенаправление на личный кабинет
        if (decodedToken.role === "ROLE_ADMIN") {
            window.location.href = "admin_active_reservations.html";
        } else {
            window.location.href = "my_profile.html";
        }

    } catch (error) {
        console.error("Login Error:", error);
    }
}

// Функция декодирования JWT
function parseJwt(token) {
    try {
        const base64Url = token.split('.')[1];
        const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
        const jsonPayload = decodeURIComponent(
            atob(base64)
                .split('')
                .map(c => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
                .join('')
        );
        return JSON.parse(jsonPayload);
    } catch (error) {
        console.error("Error decoding JWT:", error);
        return null;
    }
}

  
  document.querySelector(".sign-in form").addEventListener("submit", handleLogin);