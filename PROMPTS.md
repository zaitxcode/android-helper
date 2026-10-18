# PROMPTS.md

Reusable instructions for working with AI agents on this project (or any
Android/Kotlin project). Copy the relevant blocks into a prompt when starting a
task. Written for both humans and agents.

---

## 1. The Golden Rule

Paste this at the top of any non-trivial task. It is the single most valuable
instruction — it prevents an agent from making fast, destructive changes.

```
Before doing anything:
1. Analyze and fully understand the project first.
2. Produce a written plan.
3. Show me the plan and wait for approval.
Do not execute a single change before that.
```

---

## 2. Core Working Agreement

A complete, copy-paste block for any AI agent.

```
Before executing anything, follow these rules:

[PLANNING]
- Analyze the project first, then present a plan before any modification.
- Do not assume. Inspect the actual files and read the real output.
- If a command fails, read the actual error; never guess.

[QUALITY]
- Everything in English: identifiers, comments, strings, documentation.
- Leave no dead code, no commented-out blocks, no placeholder stubs.
- Write final content directly to the target file. No scratch or staging
  files (.tmp, .bak, .orig, _draft, _new, _additions).

[CLEANLINESS]
- Delete build output and caches: build/, .gradle/, .kotlin/, .dart_tool/.
- Never commit build directories to Git.

[VERIFICATION]
- Do not say "it works". Actually build it, run the tests, and show the output.
- Report real command output, not expectations.

[VERSIONING]
- When changing a version, update it in every location together
  (version catalog, jitpack.yml, pubspec.yaml, build files, README).
- Never leave a mix of old and new versions.

[DOCUMENTATION & AGENTS.MD]
- MANDATORY: Always update the Project Directory Tree and Change Log in AGENTS.md
  whenever you create, move, or delete files, or when architectural changes occur.
- Keep AGENTS.md as the living single source of truth for repository state and structure.

[PUBLISHING]
- A release means three things together: commit + tag + GitHub Release.
```

---

## 3. Android Library Rules

Apply these when the task touches a published library.

```
[OBFUSCATION AND CONSUMER SAFETY]
- Keep obfuscation OFF for the library and demo modules so consumers can call
  the API safely with no extra configuration.
- Keep the rule files (proguard-rules.pro, consumer-rules.pro) as documentation
  and as a safety net for consumers who enable their own minification.
- Rule: never rename or strip the public surface (com.zaitxcode.*).

[JAVA INTEROP]
- Every public helper must be callable cleanly from plain Java
  (@JvmStatic / @JvmOverloads).
- Provide a Java example that covers every module.

[IDENTITY]
- Always preserve the namespace (e.g. com.zaitxcode.*) in every file that
  belongs to the library.

[API SAFETY]
- Helpers must fail safe: catch internal exceptions and return a safe fallback
  (false / null / "" / empty list). Callers should never need try/catch.
```

---

## 4. Quick One-Liners

Short commands to reuse per situation.

| Situation | What to say |
|---|---|
| New task | `Analyze the project, then present a plan before executing.` |
| After approval | `Start.` / `Continue from where you stopped.` |
| Cleanup | `Clean caches, build output, and anything unused.` |
| Language | `Everything in English, including comments.` |
| Verification | `Verify everything with a real build. Do not guess.` |
| Versioning | `Bump the version to X in every location.` |
| Publishing | `Push to GitHub with a tag and a release.` |
| Licensing | `Apply the license, preserving the identity and the namespace.` |

---

## 5. The Three Highest-Value Templates

**① Before any task**
```
Analyze and understand the project. Do not execute anything before presenting
a written plan and getting approval.
```

**② To guarantee quality**
```
Everything in English. No dead code. Build and verify for real — never claim
"it works" without proof.
```

**③ Before publishing**
```
Verify Git and the account, then: commit + tag + GitHub Release. Keep the
version synchronized across every file.
```

---

## 6. Principles Behind These Rules

The common thread across all of the above:

> Do not trust an AI's speed — plan, clean, verify, then publish.

- **Plan first.** A written plan surfaces misunderstandings before damage.
- **Verify, don't assume.** Real build output is the only proof.
- **Clean as you go.** Caches and dead code hide real problems.
- **One source of truth.** Versions and namespaces must never drift.
- **Preserve identity.** Attribution and namespaces are non-negotiable.
