import { useEffect, useMemo, useState } from 'react'
import './App.css'

const PROMPTS_URL = import.meta.env.VITE_PROMPT_API_URL || '/api/prompts'
const REVIEWS_URL = import.meta.env.VITE_REVIEW_API_URL || '/api/reviews'
const emptyPrompt = { name: '', description: '', content: '', tags: '', modelTarget: '' }
const emptyReview = { reviewerName: '', score: 5, feedback: '' }

async function request(url, options = {}) {
  const response = await fetch(url, {
    headers: { 'Content-Type': 'application/json', ...options.headers },
    ...options,
  })
  if (response.status === 204) return null
  const data = await response.json().catch(() => null)
  if (!response.ok) {
    const validation = data?.validationErrors && Object.values(data.validationErrors).flat().join(' ')
    throw new Error(validation || data?.message || `Request failed (${response.status})`)
  }
  return data
}

function Stars({ score }) {
  return <span className="stars" aria-label={`${score} out of 5 stars`}>{'★'.repeat(Math.round(score))}{'☆'.repeat(5 - Math.round(score))}</span>
}

function App() {
  const [prompts, setPrompts] = useState([])
  const [reviews, setReviews] = useState([])
  const [activePrompt, setActivePrompt] = useState(null)
  const [isCreating, setIsCreating] = useState(false)
  const [promptForm, setPromptForm] = useState(emptyPrompt)
  const [reviewForm, setReviewForm] = useState(emptyReview)
  const [search, setSearch] = useState('')
  const [tag, setTag] = useState('')
  const [loading, setLoading] = useState(true)
  const [saving, setSaving] = useState(false)
  const [notice, setNotice] = useState(null)

  const loadData = async () => {
    setLoading(true)
    try {
      const [promptData, reviewData] = await Promise.all([request(PROMPTS_URL), request(REVIEWS_URL)])
      setPrompts(promptData || [])
      setReviews(reviewData || [])
      setNotice(null)
    } catch (error) {
      setNotice({ type: 'error', text: `${error.message}. Make sure both backend services are running.` })
    } finally { setLoading(false) }
  }

  useEffect(() => { loadData() }, [])

  const filteredPrompts = useMemo(() => prompts.filter((prompt) => {
    const query = search.toLowerCase()
    const matchesSearch = !query || [prompt.name, prompt.description, prompt.content, prompt.tags].some((value) => value?.toLowerCase().includes(query))
    const matchesTag = !tag || prompt.tags?.toLowerCase().split(',').map((item) => item.trim()).includes(tag.toLowerCase())
    return matchesSearch && matchesTag
  }), [prompts, search, tag])

  const selectedReviews = activePrompt ? reviews.filter((review) => review.promptId === activePrompt.id) : []
  const average = selectedReviews.length ? selectedReviews.reduce((total, review) => total + review.score, 0) / selectedReviews.length : 0
  const allTags = [...new Set(prompts.flatMap((prompt) => prompt.tags?.split(',') || []).map((item) => item.trim()).filter(Boolean))]

  const selectPrompt = (prompt) => {
    setActivePrompt(prompt)
    setIsCreating(false)
    setPromptForm({ name: prompt.name || '', description: prompt.description || '', content: prompt.content || '', tags: prompt.tags || '', modelTarget: prompt.modelTarget || '' })
    setReviewForm(emptyReview)
  }
  const newPrompt = () => { setActivePrompt(null); setIsCreating(true); setPromptForm(emptyPrompt); setReviewForm(emptyReview) }
  const updateForm = (setter, field, value) => setter((form) => ({ ...form, [field]: value }))

  const savePrompt = async (event) => {
    event.preventDefault(); setSaving(true)
    try {
      const saved = await request(activePrompt ? `${PROMPTS_URL}/${activePrompt.id}` : PROMPTS_URL, { method: activePrompt ? 'PUT' : 'POST', body: JSON.stringify(promptForm) })
      setPrompts((items) => activePrompt ? items.map((item) => item.id === saved.id ? saved : item) : [saved, ...items])
      selectPrompt(saved)
      setNotice({ type: 'success', text: activePrompt ? 'Prompt updated.' : 'Prompt created. You can now add a review.' })
    } catch (error) { setNotice({ type: 'error', text: error.message }) } finally { setSaving(false) }
  }

  const removePrompt = async () => {
    if (!activePrompt || !window.confirm(`Delete “${activePrompt.name}”? This cannot be undone.`)) return
    setSaving(true)
    try {
      await request(`${PROMPTS_URL}/${activePrompt.id}`, { method: 'DELETE' })
      setPrompts((items) => items.filter((item) => item.id !== activePrompt.id)); setActivePrompt(null); setIsCreating(false); setPromptForm(emptyPrompt)
      setNotice({ type: 'success', text: 'Prompt deleted.' })
    } catch (error) { setNotice({ type: 'error', text: error.message }) } finally { setSaving(false) }
  }

  const submitReview = async (event) => {
    event.preventDefault(); if (!activePrompt) return
    setSaving(true)
    try {
      const review = await request(REVIEWS_URL, { method: 'POST', body: JSON.stringify({ ...reviewForm, promptId: activePrompt.id, score: Number(reviewForm.score) }) })
      setReviews((items) => [review, ...items]); setReviewForm(emptyReview)
      setNotice({ type: 'success', text: 'Review submitted.' })
    } catch (error) { setNotice({ type: 'error', text: error.message }) } finally { setSaving(false) }
  }

  return <main className="app-shell">
    <header className="topbar"><div className="brand"><span className="brand-mark">✦</span><span>Prompt<span>Desk</span></span></div><div className="service-status"><i></i> Services workspace</div></header>
    <section className="hero"><div><p className="eyebrow">PROMPT MANAGEMENT</p><h1>Build prompts people<br />want to use.</h1><p className="hero-copy">Create, refine, and review your AI prompts from one focused workspace.</p></div><button className="primary-button" onClick={newPrompt}>+ New prompt</button></section>
    {notice && <div className={`notice ${notice.type}`}><span>{notice.text}</span><button aria-label="Dismiss notification" onClick={() => setNotice(null)}>×</button></div>}
    <section className="workspace">
      <aside className="sidebar"><div className="sidebar-heading"><div><span>YOUR LIBRARY</span><strong>{prompts.length} prompts</strong></div><button title="Refresh data" onClick={loadData}>↻</button></div><div className="search"><span>⌕</span><input value={search} onChange={(e) => setSearch(e.target.value)} placeholder="Search prompts" /></div><div className="tag-list"><button className={!tag ? 'selected' : ''} onClick={() => setTag('')}>All prompts <span>{prompts.length}</span></button>{allTags.map((item) => <button className={tag === item ? 'selected' : ''} key={item} onClick={() => setTag(item)}># {item}</button>)}</div><div className="prompt-list">{loading ? <p className="muted">Loading library…</p> : filteredPrompts.length ? filteredPrompts.map((prompt) => <button key={prompt.id} className={`prompt-item ${activePrompt?.id === prompt.id ? 'active' : ''}`} onClick={() => selectPrompt(prompt)}><strong>{prompt.name}</strong><small>{prompt.modelTarget || 'No model target'}</small><span>{prompt.tags || 'Untagged'}</span></button>) : <p className="muted">No prompts found.</p>}</div></aside>
      <section className="editor">{activePrompt || isCreating ? <><div className="editor-title"><div><p className="eyebrow">{activePrompt ? 'EDIT PROMPT' : 'NEW PROMPT'}</p><h2>{activePrompt ? 'Refine your prompt' : 'Start with an idea'}</h2></div>{activePrompt && <button className="delete-button" disabled={saving} onClick={removePrompt}>Delete</button>}</div><form onSubmit={savePrompt} className="prompt-form"><label>Prompt name<input required value={promptForm.name} onChange={(e) => updateForm(setPromptForm, 'name', e.target.value)} placeholder="e.g. Product description writer" /></label><label>Description <span className="optional">optional</span><input value={promptForm.description} onChange={(e) => updateForm(setPromptForm, 'description', e.target.value)} placeholder="What is this prompt used for?" /></label><label className="full">Prompt content<textarea required value={promptForm.content} onChange={(e) => updateForm(setPromptForm, 'content', e.target.value)} placeholder="Write the instructions for the AI…" rows="9" /></label><div className="form-row"><label>Tags<input value={promptForm.tags} onChange={(e) => updateForm(setPromptForm, 'tags', e.target.value)} placeholder="writing, marketing" /></label><label>Model target<input value={promptForm.modelTarget} onChange={(e) => updateForm(setPromptForm, 'modelTarget', e.target.value)} placeholder="e.g. gpt-4o-mini" /></label></div><div className="form-actions"><span>{activePrompt?.updatedAt ? `Last updated ${new Date(activePrompt.updatedAt).toLocaleDateString()}` : 'Fields marked required are needed to save.'}</span><button className="primary-button" disabled={saving}>{saving ? 'Saving…' : activePrompt ? 'Save changes' : 'Create prompt'}</button></div></form></> : <div className="empty-state"><div>✦</div><h2>Select a prompt or create a new one</h2><p>Your prompt details will appear here.</p><button className="primary-button" onClick={newPrompt}>Create prompt</button></div>}</section>
      <aside className="review-panel">{activePrompt ? <><div className="review-heading"><p className="eyebrow">PROMPT REVIEW</p><h2>Feedback</h2>{selectedReviews.length > 0 ? <div className="score-summary"><strong>{average.toFixed(1)}</strong><div><Stars score={average} /><span>{selectedReviews.length} review{selectedReviews.length !== 1 && 's'}</span></div></div> : <p className="muted">No reviews yet</p>}</div><form className="review-form" onSubmit={submitReview}><h3>Leave a review</h3><label>Your name<input required value={reviewForm.reviewerName} onChange={(e) => updateForm(setReviewForm, 'reviewerName', e.target.value)} placeholder="Your name" /></label><label>Score<div className="score-picker">{[1, 2, 3, 4, 5].map((value) => <button type="button" className={Number(reviewForm.score) >= value ? 'lit' : ''} onClick={() => updateForm(setReviewForm, 'score', value)} key={value}>★</button>)}</div></label><label>Feedback<textarea required rows="3" value={reviewForm.feedback} onChange={(e) => updateForm(setReviewForm, 'feedback', e.target.value)} placeholder="What worked well? What could improve?" /></label><button className="secondary-button" disabled={saving}>Submit review</button></form><div className="review-list">{selectedReviews.map((review) => <article className="review-card" key={review.id}><div><strong>{review.reviewerName}</strong><Stars score={review.score} /></div><p>{review.feedback}</p><small>{new Date(review.reviewedAt).toLocaleDateString()}</small></article>)}</div></> : <div className="review-empty"><span>☆</span><h2>Reviews live here</h2><p>Select a saved prompt to see feedback and submit a review.</p></div>}</aside>
    </section>
  </main>
}

export default App
