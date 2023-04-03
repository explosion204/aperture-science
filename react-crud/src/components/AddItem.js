import { useState } from 'react';
import { useItemContext } from '../contexts/ItemContext';

const AddItem = () => {
    const { onItemAdd } = useItemContext();
    const [title, setTitle] = useState('');
    const [description, setDescription] = useState('');
    
    const onInputChange = (e, mutator) => {
        mutator(e.target.value);
    };

    const onSubmit = e => {
        e.preventDefault();

        if (!title || !description) {
            return;
        }

        onItemAdd({ title, description, marked: false });
        setTitle('');
        setDescription('');
    };

    return (
        <form className='d-flex flex-column w-100'
              onSubmit={onSubmit}>

            <input type='text'
                   className='form-control mt-1'
                   placeholder='Title'
                   value={title}
                   onChange={e => onInputChange(e, setTitle)}/>

            <input type='text' 
                   className='form-control mt-3'
                   placeholder='Description'
                   value={description}
                   onChange={e => onInputChange(e, setDescription)}/>

            <input type='submit' 
                   className='btn mt-3 mb-1' 
                   style={{ backgroundColor: 'green', color: 'white' }} 
                   value='Save'/>
        </form>
    )
};

export default AddItem;
