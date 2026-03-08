const baseUrl = "http://localhost:8080";

// ADD PATIENT
async function addPatient() {
  await fetch(baseUrl + "/patients", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({
      name: pname.value,
      email: pemail.value,
      phone: pphone.value
    })
  });
  alert("Patient added");
}

// ADD DOCTOR
async function addDoctor() {
  await fetch(baseUrl + "/doctors", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({
      name: dname.value,
      specialization: dspec.value,
      email: demail.value
    })
  });
  alert("Doctor added");
}

// BOOK APPOINTMENT
async function bookAppointment() {
  await fetch(baseUrl + "/appointments", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({
      patient: { id: patientId.value },
      doctor: { id: doctorId.value },
      appointmentDate: date.value,
      status: "CONFIRMED"
    })
  });
  alert("Appointment booked");
}

// LOAD APPOINTMENTS
async function getAppointments() {
  const res = await fetch(baseUrl + "/appointments");
  const data = await res.json();

  appointmentsList.innerHTML = "";

  data.forEach(a => {
    appointmentsList.innerHTML += `
      <li>
        Patient: ${a.patient.id} | Doctor: ${a.doctor.id} | Date: ${a.appointmentDate}
      </li>
    `;
  });
}