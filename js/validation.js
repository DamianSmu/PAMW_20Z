window.addEventListener('load', function () {

    const submit_button = document.getElementById("submit_button");
    const form = document.getElementById("signnup_form");

    form.addEventListener('change', function () {
        if (form.checkValidity() === true) {
            submit_button.removeAttribute("disabled")
        }
    }, false);

    const username_input = document.getElementById("username");
    username_input.addEventListener('focusout', function () {
        checkLoginAvailability(username_input.value).then(data => {
            console.log(data)
            if (data[username_input.value] === 'available') {
                username_input
            }
        })
    })

}, false);


async function checkLoginAvailability(value) {
    const response = await fetch("http://infinite-hamlet-29399.herokuapp.com/check/" + value, {
        method: 'GET',
        referrerPolicy: 'no-referrer',
    }).catch((error) => {
        console.error('Error:', error);
    });
    return response.json()
}
