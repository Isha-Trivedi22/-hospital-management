import React, { useState, useEffect } from 'react';
import API from './api';

function Appointments() {
  const [appointments, setAppointments] = useState([]);
  const [form, setForm] = useState({
    patientName: '', doctorName: '', date: '', time: '', status: ''
  });

  useEffect(() => { fetchAppointments(); }, []);

  const fetchAppointments = async () => {
    try {
      const res = await API.get('/appointments');
      setAppointments(res.data);
    } catch (err) { console.error(err); }
  };

  const addAppointment = async () => {
    if (!form.patientName) return alert('Patient name required!');
    try {
      await API.post('/appointments', form);
      setForm({ patientName: '', doctorName: '', date: '', time: '', status: '' });
      fetchAppointments();
    } catch (err) { console.error(err); }
  };

  const deleteAppointment = async (id) => {
    await API.delete(`/appointments/${id}`);
    fetchAppointments();
  };

  return (
    <div>
      <h2>📅 Appointment Management</h2>
      <div className="form-row">
        <input placeholder="Patient Name" value={form.patientName}
          onChange={e => setForm({...form, patientName: e.target.value})} />
        <input placeholder="Doctor Name" value={form.doctorName}
          onChange={e => setForm({...form, doctorName: e.target.value})} />
        <input placeholder="Date" value={form.date} type="date"
          onChange={e => setForm({...form, date: e.target.value})} />
        <input placeholder="Time" value={form.time}
          onChange={e => setForm({...form, time: e.target.value})} />
        <input placeholder="Status" value={form.status}
          onChange={e => setForm({...form, status: e.target.value})} />
        <button className="btn-add" onClick={addAppointment}>➕ Book</button>
      </div>
      <table>
        <thead>
          <tr><th>ID</th><th>Patient</th><th>Doctor</th>
              <th>Date</th><th>Time</th><th>Status</th><th>Action</th></tr>
        </thead>
        <tbody>
          {appointments.map(a => (
            <tr key={a.id}>
              <td>{a.id}</td><td>{a.patientName}</td><td>{a.doctorName}</td>
              <td>{a.date}</td><td>{a.time}</td><td>{a.status}</td>
              <td><button className="btn-delete"
                onClick={() => deleteAppointment(a.id)}>🗑 Cancel</button></td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default Appointments;