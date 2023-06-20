package com.stackroute.keepnote.dao;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.stackroute.keepnote.exception.NoteNotFoundException;
import com.stackroute.keepnote.model.Note;


@Repository
@Transactional
public class NoteDAOImpl implements NoteDAO {

	
	@Autowired
	SessionFactory sessionFactory;

	public NoteDAOImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	private Session getSession() {
		return sessionFactory.getCurrentSession();
	}
	

	public boolean createNote(Note note) {
		getSession().save(note);
		return Boolean.TRUE;
	}

	
	public boolean deleteNote(int noteId) {
		try {
			getSession().delete(getNoteById(noteId));
		} catch (NoteNotFoundException e) {
			e.printStackTrace();
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

	

	public List<Note> getAllNotesByUserId(String userId) {
		CriteriaBuilder cb= getSession().getCriteriaBuilder();
		CriteriaQuery<Note> cq=cb.createQuery(Note.class);
		Root<Note> note=cq.from(Note.class);
		cq.select(note).where(cb.equal(note.get("createdBy"), userId));
		return getSession().createQuery(cq).getResultList();
	}

	

	public Note getNoteById(int noteId) throws NoteNotFoundException {
		Note note=getSession().get(Note.class, noteId);
		if(note ==null) {
			throw new NoteNotFoundException("Note not found for: "+noteId);
		}
		return note;
	}

	
	public boolean UpdateNote(Note note) {
		getSession().update(note);
		return Boolean.TRUE;
	}

}
