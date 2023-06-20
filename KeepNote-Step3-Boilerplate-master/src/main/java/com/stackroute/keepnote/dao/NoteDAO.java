package com.stackroute.keepnote.dao;

import java.util.List;

import com.stackroute.keepnote.exception.NoteNotFoundException;
import com.stackroute.keepnote.model.Note;

public interface NoteDAO {

	
	public boolean createNote(Note note);

	public boolean deleteNote(int noteId);

	public List<Note> getAllNotesByUserId(String userId);

	public Note getNoteById(int noteId) throws NoteNotFoundException;

	public boolean UpdateNote(Note note);

}
