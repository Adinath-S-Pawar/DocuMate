# DocuMate

**DocuMate** is a Chrome Extension combined with a backend service that leverages AI to extract, summarize, and analyze content from any webpage. 
It helps users quickly understand and explore online content by providing structured summaries, explanations, and further reading recommendations.

---

## Features

1. **Summarizes**
   - Extracts key points from any webpage.
   - Presents clean and concise summaries for quick understanding.

2. **Explains**
   - Breaks down complex topics in plain language.
   - Offers detailed explanations for terms, concepts, or sections in the content.

3. **Provides Further Reading**
   - Suggests related articles, tutorials, or references to deepen knowledge.
   - Helps users explore topics beyond the summarized content.

---

## Demo

Watch DocuMate in action: [LinkedIn Demo Video](https://www.linkedin.com/posts/adinath-pawar-a23b662ab_when-learning-from-documentation-we-might-activity-7371773748867284992-h1oT?utm_source=share&utm_medium=member_desktop&rcm=ACoAAEq-iDsB3lApMB14xGcxtbZXORJO67xOa1U)


## Project Structure

DocuMate/
├── DocuMateMain/ # Backend service (Spring Boot)
│ ├── src/main/java/... # Backend source code
│ └── application.properties
├── DocuMateExtension/ # Chrome Extension frontend
│ └── sidePanel.js
├── .gitignore 
└── README.md

---

## Installation & Setup

### Backend Setup (Spring Boot)

1. Clone the repository:
```bash
git clone https://github.com/Adinath-S-Pawar/DocuMate.git
cd DocuMate/DocuMateMain
```

2. Create a .env file in the DocuMateMain/ folder:
```bash
echo "GEMINI_KEY=your_real_api_key" > .env
```

3. Configure application.properties to load the key:
```properties
gemini.api.key=${GEMINI_KEY}
gemini.api.uri=https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent
```

4. Run the backend:
   
### Maven:
```bash
mvn spring-boot:run
```

### Gradle:
```bash
./gradlew bootRun
```

Chrome Extension Setup
1. Open Chrome → Extensions → Enable Developer Mode.
2. Click Load unpacked → select the DocuMateExtension/ folder.
3. The extension icon appears in the toolbar.
4. Open any webpage and use the side panel to:
      * Summarize content
      * Explain content
      * Provide further reading

Environment Variables
  * GEMINI_KEY → Your AI service API key.
  * Keep sensitive keys in .env and add it to .gitignore.

Using IntelliJ IDEA
  * Add environment variable GEMINI_KEY in Run/Debug Configurations → Environment Variables.
  * Start backend from IDE or terminal (mvn spring-boot:run or ./gradlew bootRun).

Using VS Code
  * Create .env in DocuMateMain/ with your API key:
      ```GEMINI_KEY=your_api_key_here```
  * Start backend in VS Code terminal:
      ```bash
      cd DocuMateMain
      mvn spring-boot:run
      ```
     # or
     ```bash
     ./gradlew bootRun
     ```
