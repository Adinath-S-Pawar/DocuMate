document.addEventListener(   
    'DOMContentLoaded',
    () => {
    //loading saved notes
        chrome.storage.local.get(
            ['researchNotes'],   // key
            function(result)
            {
                if(result.researchNotes)
                {
                    document.getElementById('notes').value = result.researchNotes;
                }
            }
        );

        document.getElementById('summarizeBtn').addEventListener('click', summarizeText);
        document.getElementById('suggestBtn').addEventListener('click', suggestText);
        document.getElementById('explainBtn').addEventListener('click', explainText);
        document.getElementById('saveNotesBtn').addEventListener('click', saveNotes);
    }
);

async function processText(operation)
{
    try
    {
        // first tab
        const [tab] = await chrome.tabs.query({active:true, currentWindow : true});

        // result field of first element
        const [{result}] = await chrome.scripting.executeScript(
            {
                target : {tabId : tab.id},
                function : ()=> window.getSelection().toString()
            }
        );

        if (!result)
        {
            showResult('Please select some text first');
            return;
        }

        const response = await fetch(
            'http://localhost:8080/api/research/process',
            {
                method : 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({content: result, operation})

            }
        );

        if (!response.ok) 
        {
            throw new Error(`API Error: ${response.status}`);
        }

        const text = await response.text();
        showResult(text.replace(/\n/g,'<br>'));
    }
    catch(error)
    {
        showResult('Error: ' + error.message);
    }
}

function summarizeText()
{
    return processText('summarize');
}

function suggestText()
{
    return processText('suggest');
}

function explainText()
{
    return processText('explain');
}

function showResult(content) 
{
    document.getElementById('results').innerHTML = `<div class="result-item"><div class="result-content">${content}</div></div>`;
}

async function saveNotes()
{
    const notes = document.getElementById('notes').value;
    chrome.storage.local.set({'researchNotes': notes}, function(){
        alert('Notes saved successfully !');
    });
}