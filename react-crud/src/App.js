import Header from "./components/Header";
import Items from "./components/Items";
import AddItem from "./components/AddItem";
import About from './components/About';
import { useState, useEffect } from "react";
import { BrowserRouter, Route, Routes, useParams } from "react-router-dom";
import ItemDetails from "./components/ItemDetails";

const App = () => {
  const [items, setItems] = useState([]);
  const [formIsVisible, setFormIsVisible] = useState(false);
  const params = useParams();

  useEffect(() => {
    const fetchItems = async () => {
      const response = await fetch('http://localhost:5000/items');
      const fetchedItems = await response.json();
      setItems(fetchedItems);
    };

    fetchItems();
  }, []);

  const deleteItem = async id => {
    await fetch(`http://localhost:5000/items/${id}`, {
      method: 'DELETE'
    })
    const updatedItems = items.filter(i => i.id !== id);
    setItems(updatedItems);
  };

  const updateItem = async item => {
    const response = await fetch(`http://localhost:5000/items/${item.id}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(item)
    });

    const updatedItem = await response.json();
    const updatedItems = items.map(i => i.id === item.id ? updatedItem : i, items);
    setItems(updatedItems);
  };

  const addItem = async item => {
    const response = await fetch('http://localhost:5000/items', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(item)
    });

    const newItem = await response.json();
    setItems([...items, newItem]);
  };

  const onHeaderButtonClick = () => {
    setFormIsVisible(!formIsVisible);
  };

  return (
    <div className="container h-100 w-100">
      <BrowserRouter>
        <Header text="Items" formIsVisible={formIsVisible} onButtonClick={onHeaderButtonClick} />
        <Routes>
          <Route path='/' element={
            <>
              {formIsVisible && <AddItem onAdd={addItem} />}
              {
                items.length > 0
                  ? <Items items={items} onDelete={deleteItem} onUpdate={updateItem} />
                  : 'Nothing to show'
              }
            </>
          } />
          <Route path='/about' element={<About />} />
          <Route path='/item/:id' element={<ItemDetails items={items} />} />
        </Routes>
      </BrowserRouter>
    </div>
  );
}

export default App;