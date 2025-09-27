# CLAUDE Integration: `claude.md`

This document specifies the behaviour and prompts for an assistant (Claude) integrated into a personal coding project to handle research-note creation and automated commits. It defines the exact flow, edge cases, and sample prompts so Claude can act consistently.

---

## Goal

When the user asks the assistant to `commit`, the assistant must:

1. Ask the user what research/findings (if any) should be included in `research-notes.md`.
2. If the user provides nothing, add nothing to `research-notes.md`.
3. If the user gives a short topic or keywords, search the conversation history for related questions and findings and gather matching content.
4. If matches are found, create or append a section in `research-notes.md` with the relevant Q&A and findings.
5. If no matches are found, add a heading for the topic and insert helpful suggestions the user can fill in.
6. After handling the research notes, run the repository commit and push steps (see `Commit & Push` section).

---

## File format & heading conventions

* `research-notes.md` will be a Markdown file at the repo root.
* Headings created by Claude must use the following format so they can be parsed later:

```
## YYYY-MM-DD — Topic: <short-topic-slug>

<Optional: summary line>

### Found conversation excerpts
- **Q:** <question from convo>
  **A / Findings:** <assistant/user findings or excerpt>

### If nothing found: Suggestions
- Suggestion 1
- Suggestion 2

---
```

* Use ISO date for the heading date (e.g. `2025-09-27`).
* Use a short-topic-slug (kebab-case) derived from the user input.

---

## Conversation-search strategy

* Search only the current session conversation history (messages available to the assistant) for:

  * Questions asked by the user that contain the topic keywords or synonym matches.
  * Assistant replies containing findings, code snippets, or conclusions related to the topic.
* Match heuristics (in order): exact keyword match → substring match → fuzzy match on related words.
* If multiple relevant messages exist, include the most load-bearing items only (max 6 bullets) and summarize duplicates.

---

## When user input is empty

* If the user responds with an empty string or "none" or "nothing":

  * Do not modify `research-notes.md`.
  * Proceed directly to the commit & push phase.

---

## When no conversation matches are found

* Create the heading in `research-notes.md` with the date and topic.
* Under "Suggestions", include 3–6 prompts the user can answer to fill the section, e.g.:

  * "What question were you trying to answer?"
  * "Which experiments, commands, or inputs did you run?"
  * "What were the outcomes (errors, outputs, performance)?"
  * "Links to any external resources or tickets."

---

## Commit & Push

* After editing `research-notes.md` (or not, per above), run the following git commands when appropriate:

```bash
git add research-notes.md
git commit -m "docs(research): update research-notes — <short-topic-slug> — YYYY-MM-DD"
git push origin HEAD
```

* The commit message should start with `docs(research):` and include the topic slug and date when **only** the research-notes file changed.
* If `research-notes.md` was unchanged (user provided nothing and no edits were needed), skip the `git add`/`commit` steps for that file but still commit other staged changes if the user intended a broader commit. Clarify with the user if unsure.

### Commit message rules (refined)

1. **Only research-notes.md changed** →
   Use the research-note–specific message format:

   ```
   docs(research): update research-notes — <topic-slug> — YYYY-MM-DD
   ```

2. **Other files changed as well** →

   * Generate a **summary commit message** that covers the full set of staged changes.
   * The assistant should inspect `git diff --cached --name-only` to determine what is staged and then produce a concise, descriptive title line (e.g., `feat(api): add rate limiting checks`, `fix(auth): handle token expiry`, `refactor(cli): simplify argument parsing`).
   * Follow the title line with short bullet points listing the main changes (max 6 bullets). If `research-notes.md` was updated, include a bullet such as:

     * `docs: update research-notes — api-rate-limiting — 2025-09-27`
   * If multiple research topics were added in the same commit, list them under a `docs:` subsection as bullets.

3. **Message generation workflow**:

   * Detect staged files with `git diff --cached --name-only`.
   * If only `research-notes.md` is staged, use case (1).
   * Otherwise assemble a candidate commit message according to case (2).
   * Present the generated commit message to the user and **ask for final confirmation or manual edit** before running `git commit`.

4. **If the user prefers explicit control**: support two commands:

   * `commit` → perform only `git commit` (no push), after confirmation.
   * `commit and push` → perform `git commit` and `git push`, after confirmation.

### Do NOT add the following to git commit messages

* **Do not** add the following in the git commit message:

```
🤖 Generated with [Claude Code](https://claude.ai/code)

Co-Authored-By: Claude <noreply@anthropic.com>"
Create commit with descriptive message
```

* Avoid adding automated agent attribution or emoji banners in commit messages. The commit message should be descriptive, human-readable, and focused on the code/research changes.
* Always ask the user to confirm and, if needed, to manually edit the generated message before committing.

---

## Example interaction flow (exact assistant phrasing recommended)

1. User: "Please commit"
2. Assistant: "Before I commit, what research/findings should I include in `research-notes.md`? (If none, reply `none`.)"
3. User: "Add notes about 'api-rate-limiting'"
4. Assistant: "Searching conversation history for questions or findings related to `api-rate-limiting`..."

   * If found: "I found 3 relevant excerpts — I'll add them under `## 2025-09-27 — Topic: api-rate-limiting` and then include them in the commit message. Proceed? (yes/no)"
   * If none found: "I didn't find any matching conversation. I'll add a heading `## 2025-09-27 — Topic: api-rate-limiting` with suggestions for you to fill. Proceed? (yes/no)"
5. User: "yes"
6. Assistant: performs file edit, inspects staged files, generates a descriptive commit message (including research-notes update if applicable), then runs the git commands and reports back:

   * Example output if only notes changed: `Committed with message: docs(research): update research-notes — api-rate-limiting — 2025-09-27 and pushed to origin.`
   * Example output if code + notes changed:

     ```
     Committed with message:
     feat(api): add rate limiting checks
     - Updated service handler for 429 responses
     - Added unit tests for rate-limit retry
     - docs: update research-notes — api-rate-limiting — 2025-09-27
     ```

---

## Safety & user control

* Always ask for final confirmation before performing the `git commit` and `git push` steps.
* If the repo has multiple remotes/branches, ask or detect the current branch (`git rev-parse --abbrev-ref HEAD`) and confirm the remote.
* Do not expose private tokens or secrets — no direct `git push` that requires embedding credentials in the commit message or files.

---

## Implementation notes (for integrating this behaviour)

* The assistant must be able to access the conversation history programmatically.
* Provide a small helper routine pseudocode for the integration:

```text
function handleCommitRequest(userInput):
  ask: "What research/findings should I include?"
  topic = userInput.trim()
  if topic in ["", "none"]:
    proceedToCommit(skipResearchFile=True)
    return
  matches = searchConversation(topic)
  if matches:
    section = formatSection(dateToday(), topic, matches)
  else:
    section = formatEmptySectionWithSuggestions(dateToday(), topic)
  appendToFile("research-notes.md", section)
  confirm: "I'll commit and push these changes. Proceed?"
  if confirmed:
    runGitAddCommitPush("research-notes.md", topic)
```

---

## Troubleshooting

* If push fails due to remote auth: explain how to set up SSH keys or use credential helpers.
* If conflicts occur: notify user and abort commit; suggest `git pull --rebase` and retry after user resolution.

---

## Change log

* v1.1 — Updated commit-message logic and explicit ban on agent attribution in commit messages.
* v1.2 — Updated example interaction flow to use refined commit message logic.

*End of `claude.md`*
