import React, { useState, useEffect } from 'react';
import API from './api';

function Billing() {
  const [bills, setBills] = useState([]);
  const [form, setForm] = useState({
    patientName: '', doctorName: '', date: '', amount: '', status: ''
  });

  useEffect(() => { fetchBills(); }, []);

  const fetchBills = async () => {
    try {
      const res = await API.get('/bills');
      setBills(res.data);
    } catch (err) { console.error(err); }
  };

  const addBill = async () => {
    if (!form.patientName) return alert('Patient name required!');
    try {
      await API.post('/bills', {
        ...form, amount: parseFloat(form.amount) || 0
      });
      setForm({ patientName: '', doctorName: '', date: '', amount: '', status: '' });
      fetchBills();
    } catch (err) { console.error(err); }
  };

  const deleteBill = async (id) => {
    await API.delete(`/bills/${id}`);
    fetchBills();
  };

  return (
    <div>
      <h2>💰 Billing Management</h2>
      <div className="form-row">
        <input placeholder="Patient Name" value={form.patientName}
          onChange={e => setForm({...form, patientName: e.target.value})} />
        <input placeholder="Doctor Name" value={form.doctorName}
          onChange={e => setForm({...form, doctorName: e.target.value})} />
        <input placeholder="Date" value={form.date} type="date"
          onChange={e => setForm({...form, date: e.target.value})} />
        <input placeholder="Amount" value={form.amount} type="number"
          onChange={e => setForm({...form, amount: e.target.value})} />
        <input placeholder="PAID / UNPAID" value={form.status}
          onChange={e => setForm({...form, status: e.target.value})} />
        <button className="btn-add" onClick={addBill}>➕ Add Bill</button>
      </div>
      <table>
        <thead>
          <tr><th>ID</th><th>Patient</th><th>Doctor</th>
              <th>Date</th><th>Amount</th><th>Status</th><th>Action</th></tr>
        </thead>
        <tbody>
          {bills.map(b => (
            <tr key={b.id}>
              <td>{b.id}</td><td>{b.patientName}</td><td>{b.doctorName}</td>
              <td>{b.date}</td><td>₹{b.amount}</td><td>{b.status}</td>
              <td><button className="btn-delete"
                onClick={() => deleteBill(b.id)}>🗑 Delete</button></td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default Billing;