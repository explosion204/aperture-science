import Header from './components/Header';
import Items from './components/Items';
import AddItem from './components/AddItem';
import About from './components/About';
import { useState, useEffect, useReducer } from 'react';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import ItemDetails from './components/ItemDetails';

const ITEM_ACTIONS = {
  INIT: 'init-items',
  ADD: 'add-item',
  UPDATE: 'update-item',
  DELETE: 'delete-item'
};

const changeItem = async (items, action) => {
  console.log(action);
  switch (action.type) {
    case ITEM_ACTIONS.INIT: {
      return action.items;
    }

    case ITEM_ACTIONS.ADD: {
      const response = await fetch('http://localhost:5000/items', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(action.item)
      });
  
      const addedItem = await response.json();
      return [...items, addedItem];
    }

    case ITEM_ACTIONS.UPDATE: {
      const response = await fetch(`http://localhost:5000/items/${action.item.id}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(action.item)
      });
  
      const updatedItem = await response.json();
      return items.map(i => i.id === updatedItem.id ? updatedItem : i, items);
    }

    case ITEM_ACTIONS.DELETE: {
      await fetch(`http://localhost:5000/items/${action.id}`, {
        method: 'DELETE'
      })
  
      return items.filter(i => i.id !== action.id);
    }
  }
};

const App = () => {
  const [items, dispatchItems] = useReducer(changeItem, []);
  const [formVisibility, setFormVisibility] = useState(() => false);

  useEffect(() => {
    const fetchItems = async () => {
      const response = await fetch('http://localhost:5000/items');
      const fetchedItems = await response.json();
      console.log(fetchedItems);
      dispatchItems(items, { type: ITEM_ACTIONS.INIT, payload: fetchedItems })
    };

    fetchItems();
  }, []);

  const toggleFormVisibilty = () => {
    setFormVisibility(visibility => !visibility);
  };

  return (
    <div className='container h-100 w-100'>
      <BrowserRouter>
        <Header text='Items' formIsVisible={formVisibility} onButtonClick={toggleFormVisibilty} />
        <Routes>
          <Route path='/' element={
            <>
              {formVisibility && <AddItem onAdd={newItem => dispatchItems(items, { type: ITEM_ACTIONS.ADD, item: newItem })} />}
              {
                items.length > 0
                  ? <Items items={items} 
                           onDelete={deletedId => dispatchItems(items, { type: ITEM_ACTIONS.DELETE, id: deletedId })} 
                           onUpdate={updatedItem => dispatchItems(items, { type: ITEM_ACTIONS.UPDATE, item: updatedItem })} />
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