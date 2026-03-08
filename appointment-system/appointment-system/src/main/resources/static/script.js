const BASE_URL = "http://localhost:8080";

window.onload = () => {
  loadPatients();
  loadDoctors();
  loadPatientList();
  loadDoctorList();
  getAppointments();
};

function showTab(name) {
  document.querySelectorAll('.tab-content').forEach(t => t.classList.remove('active'));
  document.querySelectorAll('.tab').forEach(t => t.classList.remove('active'));
  document.getElementById('tab-' + name).classList.add('active');
  event.target.classList.add('active');
}

function showMsg(id, text, type) {
  const el = document.getElementById(id);
  el.textContent = text;
  el.className = 'msg ' + type;
  setTimeout(() => { el.textContent = ''; el.className = 'msg'; }, 3000);
}

// ─── PATIENTS ───────────────────────────────────────────
async function addPatient() {
  const name = document.getElementById("pname").value.trim();
  const email = document.getElementById("pemail").value.trim();
  const phone = document.getElementById("pphone").value.trim();

  if (!name) { showMsg('patient-msg', '⚠ Patient name is required', 'error'); return; }

  try {
    const res = await fetch(BASE_URL + "/patients", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ name, email, phone })
    });
    if (res.ok) {
      showMsg('patient-msg', '✅ Patient registered successfully!', 'success');
      document.getElementById("pname").value = "";
      document.getElementById("pemail").value = "";
      document.getElementById("pphone").value = "";
      loadPatients();
      loadPatientList();
    } else {
      showMsg('patient-msg', '❌ Failed to register patient', 'error');
    }
  } catch (e) {
    showMsg('patient-msg', '❌ Could not connect to server', 'error');
  }
}

async function loadPatients() {
  try {
    const res = await fetch(BASE_URL + "/patients");
    const data = await res.json();
    const select = document.getElementById("patientId");
    select.innerHTML = "<option value=''>-- Select Patient --</option>";
    data.forEach(p => {
      select.innerHTML += `<option value="${p.id}">${p.name}</option>`;
    });
  } catch (e) {}
}

async function loadPatientList() {
  try {
    const res = await fetch(BASE_URL + "/patients");
    const data = await res.json();
    const list = document.getElementById("patientsList");
    list.innerHTML = "";
    if (data.length === 0) {
      list.innerHTML = '<li class="empty">No patients registered yet</li>';
      return;
    }
    data.forEach(p => {
      list.innerHTML += `<li>
        <strong>${p.name}</strong><br>
        📧 ${p.email || '—'} &nbsp;|&nbsp; 📞 ${p.phone || '—'}
      </li>`;
    });
  } catch (e) {}
}

// ─── DOCTORS ────────────────────────────────────────────
async function addDoctor() {
  const name = document.getElementById("dname").value.trim();
  const specialization = document.getElementById("dspec").value.trim();
  const email = document.getElementById("demail").value.trim();

  if (!name) { showMsg('doctor-msg', '⚠ Doctor name is required', 'error'); return; }

  try {
    const res = await fetch(BASE_URL + "/doctors", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ name, specialization, email })
    });
    if (res.ok) {
      showMsg('doctor-msg', '✅ Doctor added successfully!', 'success');
      document.getElementById("dname").value = "";
      document.getElementById("dspec").value = "";
      document.getElementById("demail").value = "";
      loadDoctors();
      loadDoctorList();
    } else {
      showMsg('doctor-msg', '❌ Failed to add doctor', 'error');
    }
  } catch (e) {
    showMsg('doctor-msg', '❌ Could not connect to server', 'error');
  }
}

async function loadDoctors() {
  try {
    const res = await fetch(BASE_URL + "/doctors");
    const data = await res.json();
    const select = document.getElementById("doctorId");
    select.innerHTML = "<option value=''>-- Select Doctor --</option>";
    data.forEach(d => {
      select.innerHTML += `<option value="${d.id}">${d.name} — ${d.specialization || 'General'}</option>`;
    });
  } catch (e) {}
}

async function loadDoctorList() {
  try {
    const res = await fetch(BASE_URL + "/doctors");
    const data = await res.json();
    const list = document.getElementById("doctorsList");
    list.innerHTML = "";
    if (data.length === 0) {
      list.innerHTML = '<li class="empty">No doctors added yet</li>';
      return;
    }
    data.forEach(d => {
      list.innerHTML += `<li>
        <strong>${d.name}</strong><br>
        🏥 ${d.specialization || 'General'} &nbsp;|&nbsp; 📧 ${d.email || '—'}
      </li>`;
    });
  } catch (e) {}
}

// ─── APPOINTMENTS ────────────────────────────────────────
async function bookAppointment() {
  const patientId = document.getElementById("patientId").value;
  const doctorId = document.getElementById("doctorId").value;
  const date = document.getElementById("date").value;

  if (!patientId || !doctorId || !date) {
    showMsg('appt-msg', '⚠ Please select patient, doctor and date', 'error');
    return;
  }

  try {
    const res = await fetch(BASE_URL + "/appointments", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        patient: { id: parseInt(patientId) },
        doctor: { id: parseInt(doctorId) },
        appointmentDate: date,
        status: "CONFIRMED"
      })
    });
    if (res.ok) {
      showMsg('appt-msg', '✅ Appointment booked successfully!', 'success');
      document.getElementById("date").value = "";
      getAppointments();
    } else {
      showMsg('appt-msg', '❌ Failed to book appointment', 'error');
    }
  } catch (e) {
    showMsg('appt-msg', '❌ Could not connect to server', 'error');
  }
}

async function getAppointments() {
  try {
    const res = await fetch(BASE_URL + "/appointments");
    const data = await res.json();
    const list = document.getElementById("appointmentsList");
    list.innerHTML = "";
    if (data.length === 0) {
      list.innerHTML = '<li class="empty">No appointments booked yet</li>';
      return;
    }
    data.forEach(a => {
      list.innerHTML += `<li>
        <strong>👤 ${a.patient.name}</strong> → <strong>🩺 ${a.doctor.name}</strong><br>
        📅 ${a.appointmentDate} &nbsp;|&nbsp; ✅ ${a.status}
      </li>`;
    });
  } catch (e) {}
}






