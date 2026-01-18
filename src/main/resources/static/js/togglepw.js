function showPassword(targetID) {
  var x = document.getElementById(targetID);

  if (x.type === "password") {
    x.type = "text";
  } else {
    x.type = "password";
  }
}

function showPasswordEye(pwId, eyeId) {
  var x = document.getElementById(pwId);

  if (x.type === "password") {
    x.type = "text";
  } else {
    x.type = "password";
  }

  var y = document.getElementById(eyeId);
  y.classList.toggle('bi-eye');
  y.classList.toggle('bi-eye-slash');
}