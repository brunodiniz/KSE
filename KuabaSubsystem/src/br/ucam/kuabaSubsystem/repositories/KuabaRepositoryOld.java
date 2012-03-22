package br.ucam.kuabaSubsystem.repositories;

import java.util.List;

import br.ucam.kuabaSubsystem.kuabaModel.Argument;
import br.ucam.kuabaSubsystem.kuabaModel.Decision;
import br.ucam.kuabaSubsystem.kuabaModel.FormalModel;
import br.ucam.kuabaSubsystem.kuabaModel.Idea;
import br.ucam.kuabaSubsystem.kuabaModel.KuabaModelFactory;
import br.ucam.kuabaSubsystem.kuabaModel.Question;

public interface KuabaRepositoryOld {
	public Object getModel();
	public String getUrl();
	public void setUrl(String url);
	public KuabaModelFactory getModelFactory();
	public boolean save();
	
	public List<Question> findAllQuestions();
	
	public List<Question> findQuestions(FormalModel formalModel);
	
	public List<Question> findQuestionByText(String text);
	
	public Question findFirstQuestionByText(String text);
	
	public Question findFirstQuestionByText(Idea addressIdea, String questionText);
	
	public Idea findFirstIdeaByText(Question addressedQuestion, String ideaText);
	
	
	public List<Idea> findIdeaByText(String ideaText);
	public List<Idea> findDesignedDomainIdeas(String domainIdeaText, String designIdeaText);
	public List<Idea> findAllIdeas();
	public List<Idea> findAcceptedIdeas(Question question);
	
	public void dispose();
	
	public Decision findDecision(Question question, Idea idea); 
	public<T> List<T> findAllKuabaElement(Class<T> _class);
	List<Idea> findDomainIdeasWhereIdLike(String IdPeace);
	public List<Argument> findAllArguments();
	public List<Decision> findAllDecisions();
	public List<Idea> findRejectedIdeas(Question question);	
	public Decision findMostRecentDecision(Question question);
	
}
