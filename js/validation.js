const URL = "https://infinite-hamlet-29399.herokuapp.com"

window.addEventListener('load', () => {
    const submit_button = document.getElementById("submit_button");
    const form = document.getElementById("signnup_form");
    const username_input = document.getElementById("username");
    const username_invalid_feedback = document.getElementById("username-invalid-feedback");
    const password_repeat = document.getElementById("password_repeat");
    const password = document.getElementById("password");

    form.addEventListener('submit', event => {
        event.preventDefault();
        event.stopPropagation();
    }, false);
    username_input.addEventListener('input', checkUsernameValidity, false);
    form.addEventListener('input', checkFormValidity, false);
    password_repeat.addEventListener('input', checkPasswordRepeatValidity, false)
    submit_button.addEventListener('click', () => submitRegisterForm(), false);

    function checkPasswordRepeatValidity() {
        if (password.value !== password_repeat.value) {
            password_repeat.setAttribute("pattern", "/(?=a)b/");
        } else {
            password_repeat.removeAttribute("pattern")
        }
        form.dispatchEvent(new Event('input'));
    }

    function checkFormValidity() {
        if (form.checkValidity() === true) {
            submit_button.removeAttribute("disabled");
        } else {
            submit_button.setAttribute("disabled", "disabled");
        }
    }

    function checkUsernameValidity() {
        if (username_input.value.match("[a-z]{3,12}")) {
            checkLoginAvailability(username_input.value).then(data => {
                if (data[username_input.value] !== "available") {
                    username_invalid_feedback.textContent = "Podany użytkownik istnieje już w bazie";
                    username_input.setAttribute("pattern", "/(?=a)b/");
                } else {
                    username_input.setAttribute("pattern", "[a-z]{3,12}");
                    username_invalid_feedback.textContent = "Wpisz poprawną nazwę użytkownika";
                }
            });
        } else {
            username_invalid_feedback.textContent = "Wpisz poprawną nazwę użytkownika";
        }
        form.dispatchEvent(new Event('input'));
    }

}, false);


async function checkLoginAvailability(value) {
    let response = await fetch(URL + "/check/" + value, {
        method: 'GET',
    }).catch(error => {
        console.log('Error: ', error);
    });
    if (!response.ok) {
        throw new Error("Response error");
    }
    return response.json()
}

async function submitRegisterForm() {
    let response = await fetch(URL + "/sender/register", {
        method: 'POST',
        mode: "cors",
        body: prepareRequestBody(),
    }).catch(error => {
        console.log('Error: ', error);
    });
    if (response.ok) {
        document.getElementById("alert_success").classList.remove("alert-hidden")
        document.getElementById("signnup_form").reset();
    }
}

function prepareRequestBody() {
    let content = new FormData();
    content.append("firstname", document.getElementById("firstname").value);
    content.append("lastname", document.getElementById("lastname").value);
    content.append("password", document.getElementById("password").value);
    content.append("sex", document.getElementById("sex_M").checked ? "M" : "F");
    content.append("photo", document.getElementById("photo").files[0]);
    content.append("login", document.getElementById("username").value);
    return content;
}
