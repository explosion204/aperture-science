import React, { useState, useEffect, useContext } from 'react';

const ItemContext = React.createContext();

export const useItemContext = () => {
    return useContext(ItemContext);
};

const ItemContextProvider = ({ children }) => {
    const [items, setItems] = useState([]);

    useEffect(() => {
        const fetchItems = async () => {
            const response = await fetch('http://localhost:5000/items');
            const fetchedItems = await response.json();
            setItems(fetchedItems);
        };

        fetchItems();
    }, []);

    const onItemAdd = async item => {
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

    const onItemUpdate = async item => {
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

    const onItemDelete = async id => {
        await fetch(`http://localhost:5000/items/${id}`, {
        method: 'DELETE'
        })
        const updatedItems = items.filter(i => i.id !== id);
        setItems(updatedItems);
    };

    return (
        <ItemContext.Provider value={{ items, onItemAdd, onItemUpdate, onItemDelete }}>
            { children }
        </ItemContext.Provider>
    );
};

export default ItemContextProvider;