package com.openai.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Constants {

	public static final String FREE_PLAN = "Free";
	public static final String BASIC_PLAN = "Basic";
	public static final String PROFESSIONAL_PLAN = "Professional";
	public static final String ENTERPRISE_PLAN = "Enterprise";
	public static final String UNLIMITED_PLAN = "Unlimited";
	public static final String ELABORATE_ID = "ElaborateMessage";
	public static final String SUMMARIZE_ID = "SummarizeAction";
	public static final String IMAGEVARIATION_ID = "ImageVariation";
	public static final String CUSTOMERSERVICE_ID = "CustomerService";
	public static final String TTS = "Text2Speech";
	public static final String STT = "Speech2Text";
	public static final String SENTIMENT = "Sentiment";
	public static final String ELABORATE = "Elaborate";
	public static final String SUMMARIZE = "Summarize";
	public static final String CONVERSATION = "Conversation";
	public static final String MENTION = "Mention";
	public static final String ELABORATE_COMMAND = "/elaborate";
	public static final String SUMMARIZE_COMMAND = "/summarize";
	public static final String TEXT2IMG = "/imagine";
	public static final String TRANSLATE_CALLBACKID = "translate_code";
	public static final String RAPIDCODE_CALLBACKID = "rapid_coding";
	public static final String LANG_TRANSLATE_CALLBACKID = "LangToTranslate";
	public static final String TRANSLATE_VIEW = "codetoTranslate";
	public static final String CODETOWRITE_VIEW = "codetoWrite";
	public static final String IN_PROGRESS = "IN_PROGRESS";
	public static final String COMPLETED = "COMPLETED";
	public static final String ERROR = "ERROR";
	public static final String TRANSLATECODE = "TransLateCode";
	public static final String EXCEPTION = "EXCEPTION";
    public static final String FEEDBACK = "feedbackView";
	public static final String WRITECODE = "WriteCode";
	public static final String Summriser_Assistant_ID = "";
	public static final String Sentiment_Assistant_ID = "";
	public static final String Coder_Assistant_ID = "";
	public static final String Translator_Assistant_ID = "";
	public static final String Mention_Assistant_ID = "";
	public static final String Laptop_Check_Assistant_ID = "";
	public static final String Prompt_Assistant_ID = "";
	public static final String Live_Search_Assistant_ID = "";
	public static final String Live_Stock_Analysis_ID = "";
	public static final String Live_Stock_Sentiment_ID = "";
	public static final String WELCOMEMESSAGE = "welcomeMessage";
	public static final String QUTACOMPLETEDMESSAGE = "quotaCompleted";
	public static List<String> STOP_SEQUENCE = new ArrayList<String>(
            Arrays.asList(":stop:"));
	
}
