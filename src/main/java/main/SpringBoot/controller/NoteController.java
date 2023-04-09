package main.SpringBoot.controller;

import main.SpringBoot.Note;
import main.SpringBoot.NoteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/note")
public class NoteController {
    private final List<Note> notes = new ArrayList<>();

    private Long nextId = 1L;
    private NoteService noteService;

    @GetMapping("/HP")
    @ResponseBody
    public ModelAndView note(Model model) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("HP");
        return modelAndView;
    }
    // GET /note/list - отримати список нотаток
    @GetMapping("/list")
    //@ResponseBody
    public ModelAndView list() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("notes", notes);
        return modelAndView;
    }

   @PostMapping("/delete")
   public String deleteNoteById(@RequestParam("id") Long id, Model model) {
       try {
           Note note = notes.stream()
                   .filter(n -> n.getId().equals(id))
                   .findFirst()
                   .orElseThrow(() -> new RuntimeException("Note not found"));
           notes.remove(note);
           return "note/delete";
       } catch (RuntimeException e) {
           model.addAttribute("errorMessage", e.getMessage());
           return "note/error";
       }
   }
    @GetMapping("/edit")
    public ModelAndView showEditNotePage(/*@RequestParam */Long id/*, Model model*/) {
        // Find the note with the given ID in the list of notes
        Note note = notes.stream()
                .filter(n -> n.getId().equals(id))
                .findFirst()
                .orElse(null);

        // Add the note to the model
//        model.addAttribute("note", note);

        // Return the view with the edit note form
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("note/edit");
        modelAndView.addObject("note", note);
        return modelAndView;
    }
    @PostMapping("/edit")
    public RedirectView editNote(@ModelAttribute Note note) {
        // Find the index of the note with the given ID in the list of notes
        int noteIndex = IntStream.range(0, notes.size())
                .filter(i -> notes.get(i).getId().equals(note.getId()))
                .findFirst()
                .orElse(-1);

        // Update the note in the list of notes
        if (noteIndex != -1) {
            notes.set(noteIndex, note);
        }

        // Add the updated note to the model


        // Return the view with the updated note information
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("edit");
        modelAndView.addObject("note", note);
        return new RedirectView("/note/list");
    }


    @GetMapping("/new")
    public String showSaveForm(Model model) {
        model.addAttribute("note", new Note());
        return "note/new";
    }

    @PostMapping("/new")
    public String save(@ModelAttribute Note note, Model model) {
        note.setId(nextId++);
        notes.add(note);
        model.addAttribute("notes", notes);
        System.out.println(notes.toString());
        return "/note/new";
    }

}
