import Header from './components/Header';
import Items from './components/Items';
import AddItem from './components/AddItem';
import About from './components/About';
import { useState } from 'react';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import ItemDetails from './components/ItemDetails';
import ItemContextProvider from './contexts/ItemContext';

const App = () => {
  const [formIsVisible, setFormIsVisible] = useState(false);

  const onHeaderButtonClick = () => {
    setFormIsVisible(!formIsVisible);
  };

  return (
    <div className='container h-100 w-100'>
      <ItemContextProvider>
        <BrowserRouter>
          <Header text='Items' formIsVisible={formIsVisible} onButtonClick={onHeaderButtonClick} />
          <Routes>
            <Route path='/' element={
              <>
                {formIsVisible && <AddItem/>}
                <Items/>
              </>
            } />
            <Route path='/about' element={<About />} />
            <Route path='/item/:id' element={<ItemDetails/>} />
          </Routes>
        </BrowserRouter>
      </ItemContextProvider>
    </div>
  );
}

export default App;