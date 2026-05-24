import React, { useState, useEffect } from 'react';
import API from './api';

function Doctors() {
  const [doctors, setDoctors] = useState([]);
  const [form, setForm] = useState({
    name: '', specialization: '', phone: '', email: '', experience: ''
  });

  useEffect(() => { fetchDoctors(); }, []);

  const fetchDoctors = async () => {
    try {
      const res = await API.get('/doctors');
      setDoctors(res.data);
    } catch (err) { console.error(err); }
  };

  const addDoctor = async () => {
    if (!form.name) return alert('Name is required!');
    try {
      await API.post('/doctors', {
        ...form, experience: parseInt(form.experience) || 0
      });
      setForm({ name: '', specialization: '', phone: '', email: '', experience: '' });
      fetchDoctors();
    } catch (err) { console.error(err); }
  };

  const deleteDoctor = async (id) => {
    await API.delete(`/doctors/${id}`);
    fetchDoctors();
  };

  return (
    <div>
      <h2>👨 Doctor Management</h2>
      <div className="form-row">
        <input placeholder="Name" value={form.name}
          onChange={e => setForm({...form, name: e.target.value})} />
        <input placeholder="Specialization" value={form.specialization}
          onChange={e => setForm({...form, specialization: e.target.value})} />
        <input placeholder="Phone" value={form.phone}
          onChange={e => setForm({...form, phone: e.target.value})} />
        <input placeholder="Email" value={form.email}
          onChange={e => setForm({...form, email: e.target.value})} />
        <input placeholder="Experience (yrs)" value={form.experience} type="number"
          onChange={e => setForm({...form, experience: e.target.value})} />
        <button className="btn-add" onClick={addDoctor}>➕ Add</button>
      </div>
      <table>
        <thead>
          <tr><th>ID</th><th>Name</th><th>Specialization</th>
              <th>Phone</th><th>Email</th><th>Experience</th><th>Action</th></tr>
        </thead>
        <tbody>
          {doctors.map(d => (
            <tr key={d.id}>
              <td>{d.id}</td><td>{d.name}</td><td>{d.specialization}</td>
              <td>{d.phone}</td><td>{d.email}</td><td>{d.experience}</td>
              <td><button className="btn-delete"
                onClick={() => deleteDoctor(d.id)}>🗑 Delete</button></td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default Doctors;