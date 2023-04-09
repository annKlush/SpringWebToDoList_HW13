package main.SpringBoot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class NoteService {
    private Map<Long, Note> notes = new HashMap<>();
    private long nextId = 1;

    public List<Note> listAll() {
        return new ArrayList<>(notes.values());
    }

    public Note add(Note note) {
        note.setId(nextId++);
        notes.put(note.getId(), note);
        return note;
    }

    public void deleteById(long id) {
        if (!notes.containsKey(id)) {
            throw new IllegalArgumentException("Note with id " + id + " not found.");
        }
        notes.remove(id);
    }

    public void update(Note note) {
        if (!notes.containsKey(note.getId())) {
            throw new IllegalArgumentException("Note with id " + note.getId() + " not found.");
        }
        notes.put(note.getId(), note);
    }

    public Note getById(long id) {
        Note note = notes.get(id);
        if (note == null) {
            throw new IllegalArgumentException("Note with id " + id + " not found.");
        }
        return note;
    }
}