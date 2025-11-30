package com.PagePilot.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class ResearchService
{
    @Value("${gemini.api.url}") //injecting from application.properties
    private String geminiApiUrl;

    @Value("${gemini.api.key}")
    private String geminiApikey;

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public ResearchService(WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
        this.webClient = webClientBuilder.build();
        this.objectMapper = objectMapper;
    }

    public String processContent(ResearchRequest request)
    {
        // build prompt
        String prompt = buildPrompt(request);

        // Query AI model API
        Map<String, Object> requestBody = Map.of(
                "contents", new Object[]{
                        Map.of("parts",new Object[]
                                    {
                                            Map.of("text",prompt)
                                    }
                                )
                }
        );

        //make api call
        String response = webClient.post()
                .uri(geminiApiUrl + geminiApikey)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        //cleaning markdown
        String cleanedResponse = cleanMarkdown(response);

        // parse and return the response

        return extractTextFromResponse(cleanedResponse);

    }

    private String buildPrompt(ResearchRequest request)
    {
        StringBuilder prompt = new StringBuilder();

        switch(request.getOperation())
        {
            case "summarize" :
                    prompt.append("Summarize the following content clearly and concisely" +
                            " highlighting key concepts:\n");
                    break;
            case  "suggest" :
                    prompt.append("Based on following content suggest related document for further" +
                    "reading . Format response with clear heading and bullet points\n ");
                    break;
            case  "explain" :
                prompt.append("Explain the following content in simple terms:\n");
                break;
            default:
                throw new IllegalArgumentException("Unknown Operation "+ request.getOperation());

        }
        prompt.append(request.getContent());
        return prompt.toString();
    }

    public String cleanMarkdown(String input)
    {
        if (input == null) return null;

        // Remove bold (**text**) and italic (*text*)
        String noMarkdown = input.replaceAll("\\*\\*(.*?)\\*\\*", "$1")
                .replaceAll("\\*(.*?)\\*", "$1");
        return noMarkdown;
    }

    private String extractTextFromResponse(String response)
    {
        //response mapped to GeminiResponse class using ObjectMapper
        try
        {//deserialize the JSON response (a String) into a Java object of type GeminiResponse
            GeminiResponse geminiResponse = objectMapper.readValue(response,GeminiResponse.class);

            if(geminiResponse.getCandidates() != null && !geminiResponse.getCandidates().isEmpty())
            {
                GeminiResponse.Candidate  firstCandidate = geminiResponse.getCandidates().get(0);

                //If skipped: will get a NullPointerException
                if(firstCandidate.getContent() != null &&
                firstCandidate.getContent().getParts() != null &&
                !firstCandidate.getContent().getParts().isEmpty() )
                {
                    StringBuilder fullText = new StringBuilder();
                    for (GeminiResponse.Part part : firstCandidate.getContent().getParts()) {
                        if (part.getText() != null && !part.getText().isEmpty()) {
                            fullText.append(part.getText());
                            return fullText.toString();
                        }
                    }

                }

            }
            return "No content found in response";
        }
        catch (Exception e)
        {
            return "Error Parsing : " + e.getMessage();
        }
    }

}
