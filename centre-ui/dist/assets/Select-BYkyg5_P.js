import{O as S,a as _,ah as we,J as tn,d as fe,S as an,D as u,ak as cn,ao as lt,bA as ot,P as Ke,bO as it,bP as rt,L as Y,aN as ln,aM as Be,bQ as Ye,a5 as ye,bb as Cn,ae as Fe,T as Sn,af as at,z as P,B as V,C as le,A as se,ac as on,b3 as Rn,aA as fn,aK as st,ba as dt,U as ut,E as sn,aC as Fn,H as Te,bR as ct,aL as me,bF as $e,M as dn,aF as Tn,bS as ft,F as ht,bT as vt,am as gt,w as bt,v as pt,b4 as hn,bU as mt,aB as wt,b6 as yt,b7 as xt,bV as Ct,ap as ae}from"./index-D_-DzWhp.js";import{i as un,b as St,B as Rt,V as Ft,a as Tt,u as rn}from"./Follower-CPJzzLbY.js";import{N as Ot,u as Mt}from"./Input-Bb-Nkj9P.js";import{N as Je}from"./Tag-wiA3KfAO.js";import{V as vn}from"./index-CxXG09Nk.js";import{_ as It}from"./Popover-uiGA841y.js";import{c as Qe,a as kt}from"./cssr-BW6v_MNg.js";import{b as zt}from"./next-frame-once-C5Ksf8W7.js";import{a as Pt,h as Ee,c as _t}from"./create-B6F92WxY.js";import{_ as Bt}from"./Empty-DLPWk6jr.js";import{u as gn}from"./use-merged-state-DuV1Lu3L.js";import{u as $t}from"./use-compitable-Cr7qxnTd.js";function bn(e){return e&-e}class On{constructor(t,l){this.l=t,this.min=l;const o=new Array(t+1);for(let i=0;i<t+1;++i)o[i]=0;this.ft=o}add(t,l){if(l===0)return;const{l:o,ft:i}=this;for(t+=1;t<=o;)i[t]+=l,t+=bn(t)}get(t){return this.sum(t+1)-this.sum(t)}sum(t){if(t===void 0&&(t=this.l),t<=0)return 0;const{ft:l,min:o,l:i}=this;if(t>i)throw new Error("[FinweckTree.sum]: `i` is larger than length.");let f=t*o;for(;t>0;)f+=l[t],t-=bn(t);return f}getBound(t){let l=0,o=this.l;for(;o>l;){const i=Math.floor((l+o)/2),f=this.sum(i);if(f>t){o=i;continue}else if(f<t){if(l===i)return this.sum(l+1)<=t?l+1:i;l=i}else return i}return l}}let je;function Et(){return typeof document>"u"?!1:(je===void 0&&("matchMedia"in window?je=window.matchMedia("(pointer:coarse)").matches:je=!1),je)}let Ze;function pn(){return typeof document>"u"?1:(Ze===void 0&&(Ze="chrome"in window?window.devicePixelRatio:1),Ze)}const Mn="VVirtualListXScroll";function At({columnsRef:e,renderColRef:t,renderItemWithColsRef:l}){const o=S(0),i=S(0),f=_(()=>{const m=e.value;if(m.length===0)return null;const w=new On(m.length,0);return m.forEach((B,R)=>{w.add(R,B.width)}),w}),v=we(()=>{const m=f.value;return m!==null?Math.max(m.getBound(i.value)-1,0):0}),d=m=>{const w=f.value;return w!==null?w.sum(m):0},p=we(()=>{const m=f.value;return m!==null?Math.min(m.getBound(i.value+o.value)+1,e.value.length-1):0});return tn(Mn,{startIndexRef:v,endIndexRef:p,columnsRef:e,renderColRef:t,renderItemWithColsRef:l,getLeft:d}),{listWidthRef:o,scrollLeftRef:i}}const mn=fe({name:"VirtualListRow",props:{index:{type:Number,required:!0},item:{type:Object,required:!0}},setup(){const{startIndexRef:e,endIndexRef:t,columnsRef:l,getLeft:o,renderColRef:i,renderItemWithColsRef:f}=an(Mn);return{startIndex:e,endIndex:t,columns:l,renderCol:i,renderItemWithCols:f,getLeft:o}},render(){const{startIndex:e,endIndex:t,columns:l,renderCol:o,renderItemWithCols:i,getLeft:f,item:v}=this;if(i!=null)return i({itemIndex:this.index,startColIndex:e,endColIndex:t,allColumns:l,item:v,getLeft:f});if(o!=null){const d=[];for(let p=e;p<=t;++p){const m=l[p];d.push(o({column:m,left:f(p),item:v}))}return d}return null}}),Nt=Qe(".v-vl",{maxHeight:"inherit",height:"100%",overflow:"auto",minWidth:"1px"},[Qe("&:not(.v-vl--show-scrollbar)",{scrollbarWidth:"none"},[Qe("&::-webkit-scrollbar, &::-webkit-scrollbar-track-piece, &::-webkit-scrollbar-thumb",{width:0,height:0,display:"none"})])]),Lt=fe({name:"VirtualList",inheritAttrs:!1,props:{showScrollbar:{type:Boolean,default:!0},columns:{type:Array,default:()=>[]},renderCol:Function,renderItemWithCols:Function,items:{type:Array,default:()=>[]},itemSize:{type:Number,required:!0},itemResizable:Boolean,itemsStyle:[String,Object],visibleItemsTag:{type:[String,Object],default:"div"},visibleItemsProps:Object,ignoreItemResize:Boolean,onScroll:Function,onWheel:Function,onResize:Function,defaultScrollKey:[Number,String],defaultScrollIndex:Number,keyField:{type:String,default:"key"},paddingTop:{type:[Number,String],default:0},paddingBottom:{type:[Number,String],default:0}},setup(e){const t=ot();Nt.mount({id:"vueuc/virtual-list",head:!0,anchorMetaName:kt,ssr:t}),Ke(()=>{const{defaultScrollIndex:a,defaultScrollKey:b}=e;a!=null?q({index:a}):b!=null&&q({key:b})});let l=!1,o=!1;it(()=>{if(l=!1,!o){o=!0;return}q({top:x.value,left:v.value})}),rt(()=>{l=!0,o||(o=!0)});const i=we(()=>{if(e.renderCol==null&&e.renderItemWithCols==null||e.columns.length===0)return;let a=0;return e.columns.forEach(b=>{a+=b.width}),a}),f=_(()=>{const a=new Map,{keyField:b}=e;return e.items.forEach((k,A)=>{a.set(k[b],A)}),a}),{scrollLeftRef:v,listWidthRef:d}=At({columnsRef:Y(e,"columns"),renderColRef:Y(e,"renderCol"),renderItemWithColsRef:Y(e,"renderItemWithCols")}),p=S(null),m=S(void 0),w=new Map,B=_(()=>{const{items:a,itemSize:b,keyField:k}=e,A=new On(a.length,b);return a.forEach((K,L)=>{const D=K[k],$=w.get(D);$!==void 0&&A.add(L,$)}),A}),R=S(0),x=S(0),C=we(()=>Math.max(B.value.getBound(x.value-ln(e.paddingTop))-1,0)),j=_(()=>{const{value:a}=m;if(a===void 0)return[];const{items:b,itemSize:k}=e,A=C.value,K=Math.min(A+Math.ceil(a/k+1),b.length-1),L=[];for(let D=A;D<=K;++D)L.push(b[D]);return L}),q=(a,b)=>{if(typeof a=="number"){W(a,b,"auto");return}const{left:k,top:A,index:K,key:L,position:D,behavior:$,debounce:G=!0}=a;if(k!==void 0||A!==void 0)W(k,A,$);else if(K!==void 0)N(K,$,G);else if(L!==void 0){const s=f.value.get(L);s!==void 0&&N(s,$,G)}else D==="bottom"?W(0,Number.MAX_SAFE_INTEGER,$):D==="top"&&W(0,0,$)};let I,T=null;function N(a,b,k){const{value:A}=B,K=A.sum(a)+ln(e.paddingTop);if(!k)p.value.scrollTo({left:0,top:K,behavior:b});else{I=a,T!==null&&window.clearTimeout(T),T=window.setTimeout(()=>{I=void 0,T=null},16);const{scrollTop:L,offsetHeight:D}=p.value;if(K>L){const $=A.get(a);K+$<=L+D||p.value.scrollTo({left:0,top:K+$-D,behavior:b})}else p.value.scrollTo({left:0,top:K,behavior:b})}}function W(a,b,k){p.value.scrollTo({left:a,top:b,behavior:k})}function H(a,b){var k,A,K;if(l||e.ignoreItemResize||ie(b.target))return;const{value:L}=B,D=f.value.get(a),$=L.get(D),G=(K=(A=(k=b.borderBoxSize)===null||k===void 0?void 0:k[0])===null||A===void 0?void 0:A.blockSize)!==null&&K!==void 0?K:b.contentRect.height;if(G===$)return;G-e.itemSize===0?w.delete(a):w.set(a,G-e.itemSize);const h=G-$;if(h===0)return;L.add(D,h);const E=p.value;if(E!=null){if(I===void 0){const ne=L.sum(D);E.scrollTop>ne&&E.scrollBy(0,h)}else if(D<I)E.scrollBy(0,h);else if(D===I){const ne=L.sum(D);G+ne>E.scrollTop+E.offsetHeight&&E.scrollBy(0,h)}ee()}R.value++}const Z=!Et();let J=!1;function de(a){var b;(b=e.onScroll)===null||b===void 0||b.call(e,a),(!Z||!J)&&ee()}function ue(a){var b;if((b=e.onWheel)===null||b===void 0||b.call(e,a),Z){const k=p.value;if(k!=null){if(a.deltaX===0&&(k.scrollTop===0&&a.deltaY<=0||k.scrollTop+k.offsetHeight>=k.scrollHeight&&a.deltaY>=0))return;a.preventDefault(),k.scrollTop+=a.deltaY/pn(),k.scrollLeft+=a.deltaX/pn(),ee(),J=!0,zt(()=>{J=!1})}}}function oe(a){if(l||ie(a.target))return;if(e.renderCol==null&&e.renderItemWithCols==null){if(a.contentRect.height===m.value)return}else if(a.contentRect.height===m.value&&a.contentRect.width===d.value)return;m.value=a.contentRect.height,d.value=a.contentRect.width;const{onResize:b}=e;b!==void 0&&b(a)}function ee(){const{value:a}=p;a!=null&&(x.value=a.scrollTop,v.value=a.scrollLeft)}function ie(a){let b=a;for(;b!==null;){if(b.style.display==="none")return!0;b=b.parentElement}return!1}return{listHeight:m,listStyle:{overflow:"auto"},keyToIndex:f,itemsStyle:_(()=>{const{itemResizable:a}=e,b=Be(B.value.sum());return R.value,[e.itemsStyle,{boxSizing:"content-box",width:Be(i.value),height:a?"":b,minHeight:a?b:"",paddingTop:Be(e.paddingTop),paddingBottom:Be(e.paddingBottom)}]}),visibleItemsStyle:_(()=>(R.value,{transform:`translateY(${Be(B.value.sum(C.value))})`})),viewportItems:j,listElRef:p,itemsElRef:S(null),scrollTo:q,handleListResize:oe,handleListScroll:de,handleListWheel:ue,handleItemResize:H}},render(){const{itemResizable:e,keyField:t,keyToIndex:l,visibleItemsTag:o}=this;return u(cn,{onResize:this.handleListResize},{default:()=>{var i,f;return u("div",lt(this.$attrs,{class:["v-vl",this.showScrollbar&&"v-vl--show-scrollbar"],onScroll:this.handleListScroll,onWheel:this.handleListWheel,ref:"listElRef"}),[this.items.length!==0?u("div",{ref:"itemsElRef",class:"v-vl-items",style:this.itemsStyle},[u(o,Object.assign({class:"v-vl-visible-items",style:this.visibleItemsStyle},this.visibleItemsProps),{default:()=>{const{renderCol:v,renderItemWithCols:d}=this;return this.viewportItems.map(p=>{const m=p[t],w=l.get(m),B=v!=null?u(mn,{index:w,item:p}):void 0,R=d!=null?u(mn,{index:w,item:p}):void 0,x=this.$slots.default({item:p,renderedCols:B,renderedItemWithCols:R,index:w})[0];return e?u(cn,{key:m,onResize:C=>this.handleItemResize(m,C)},{default:()=>x}):(x.key=m,x)})}})]):(f=(i=this.$slots).empty)===null||f===void 0?void 0:f.call(i)])}})}});function In(e,t){t&&(Ke(()=>{const{value:l}=e;l&&Ye.registerHandler(l,t)}),ye(e,(l,o)=>{o&&Ye.unregisterHandler(o)},{deep:!1}),Cn(()=>{const{value:l}=e;l&&Ye.unregisterHandler(l)}))}function wn(e){switch(typeof e){case"string":return e||void 0;case"number":return String(e);default:return}}function en(e){const t=e.filter(l=>l!==void 0);if(t.length!==0)return t.length===1?t[0]:l=>{e.forEach(o=>{o&&o(l)})}}const Dt=fe({name:"Checkmark",render(){return u("svg",{xmlns:"http://www.w3.org/2000/svg",viewBox:"0 0 16 16"},u("g",{fill:"none"},u("path",{d:"M14.046 3.486a.75.75 0 0 1-.032 1.06l-7.93 7.474a.85.85 0 0 1-1.188-.022l-2.68-2.72a.75.75 0 1 1 1.068-1.053l2.234 2.267l7.468-7.038a.75.75 0 0 1 1.06.032z",fill:"currentColor"})))}}),Vt=fe({props:{onFocus:Function,onBlur:Function},setup(e){return()=>u("div",{style:"width: 0; height: 0",tabindex:0,onFocus:e.onFocus,onBlur:e.onBlur})}}),yn=fe({name:"NBaseSelectGroupHeader",props:{clsPrefix:{type:String,required:!0},tmNode:{type:Object,required:!0}},setup(){const{renderLabelRef:e,renderOptionRef:t,labelFieldRef:l,nodePropsRef:o}=an(un);return{labelField:l,nodeProps:o,renderLabel:e,renderOption:t}},render(){const{clsPrefix:e,renderLabel:t,renderOption:l,nodeProps:o,tmNode:{rawNode:i}}=this,f=o==null?void 0:o(i),v=t?t(i,!1):Fe(i[this.labelField],i,!1),d=u("div",Object.assign({},f,{class:[`${e}-base-select-group-header`,f==null?void 0:f.class]}),v);return i.render?i.render({node:d,option:i}):l?l({node:d,option:i,selected:!1}):d}});function Wt(e,t){return u(Sn,{name:"fade-in-scale-up-transition"},{default:()=>e?u(at,{clsPrefix:t,class:`${t}-base-select-option__check`},{default:()=>u(Dt)}):null})}const xn=fe({name:"NBaseSelectOption",props:{clsPrefix:{type:String,required:!0},tmNode:{type:Object,required:!0}},setup(e){const{valueRef:t,pendingTmNodeRef:l,multipleRef:o,valueSetRef:i,renderLabelRef:f,renderOptionRef:v,labelFieldRef:d,valueFieldRef:p,showCheckmarkRef:m,nodePropsRef:w,handleOptionClick:B,handleOptionMouseEnter:R}=an(un),x=we(()=>{const{value:I}=l;return I?e.tmNode.key===I.key:!1});function C(I){const{tmNode:T}=e;T.disabled||B(I,T)}function j(I){const{tmNode:T}=e;T.disabled||R(I,T)}function q(I){const{tmNode:T}=e,{value:N}=x;T.disabled||N||R(I,T)}return{multiple:o,isGrouped:we(()=>{const{tmNode:I}=e,{parent:T}=I;return T&&T.rawNode.type==="group"}),showCheckmark:m,nodeProps:w,isPending:x,isSelected:we(()=>{const{value:I}=t,{value:T}=o;if(I===null)return!1;const N=e.tmNode.rawNode[p.value];if(T){const{value:W}=i;return W.has(N)}else return I===N}),labelField:d,renderLabel:f,renderOption:v,handleMouseMove:q,handleMouseEnter:j,handleClick:C}},render(){const{clsPrefix:e,tmNode:{rawNode:t},isSelected:l,isPending:o,isGrouped:i,showCheckmark:f,nodeProps:v,renderOption:d,renderLabel:p,handleClick:m,handleMouseEnter:w,handleMouseMove:B}=this,R=Wt(l,e),x=p?[p(t,l),f&&R]:[Fe(t[this.labelField],t,l),f&&R],C=v==null?void 0:v(t),j=u("div",Object.assign({},C,{class:[`${e}-base-select-option`,t.class,C==null?void 0:C.class,{[`${e}-base-select-option--disabled`]:t.disabled,[`${e}-base-select-option--selected`]:l,[`${e}-base-select-option--grouped`]:i,[`${e}-base-select-option--pending`]:o,[`${e}-base-select-option--show-checkmark`]:f}],style:[(C==null?void 0:C.style)||"",t.style||""],onClick:en([m,C==null?void 0:C.onClick]),onMouseenter:en([w,C==null?void 0:C.onMouseenter]),onMousemove:en([B,C==null?void 0:C.onMousemove])}),u("div",{class:`${e}-base-select-option__content`},x));return t.render?t.render({node:j,option:t,selected:l}):d?d({node:j,option:t,selected:l}):j}}),jt=P("base-select-menu",`
 line-height: 1.5;
 outline: none;
 z-index: 0;
 position: relative;
 border-radius: var(--n-border-radius);
 transition:
 background-color .3s var(--n-bezier),
 box-shadow .3s var(--n-bezier);
 background-color: var(--n-color);
`,[P("scrollbar",`
 max-height: var(--n-height);
 `),P("virtual-list",`
 max-height: var(--n-height);
 `),P("base-select-option",`
 min-height: var(--n-option-height);
 font-size: var(--n-option-font-size);
 display: flex;
 align-items: center;
 `,[V("content",`
 z-index: 1;
 white-space: nowrap;
 text-overflow: ellipsis;
 overflow: hidden;
 `)]),P("base-select-group-header",`
 min-height: var(--n-option-height);
 font-size: .93em;
 display: flex;
 align-items: center;
 `),P("base-select-menu-option-wrapper",`
 position: relative;
 width: 100%;
 `),V("loading, empty",`
 display: flex;
 padding: 12px 32px;
 flex: 1;
 justify-content: center;
 `),V("loading",`
 color: var(--n-loading-color);
 font-size: var(--n-loading-size);
 `),V("header",`
 padding: 8px var(--n-option-padding-left);
 font-size: var(--n-option-font-size);
 transition: 
 color .3s var(--n-bezier),
 border-color .3s var(--n-bezier);
 border-bottom: 1px solid var(--n-action-divider-color);
 color: var(--n-action-text-color);
 `),V("action",`
 padding: 8px var(--n-option-padding-left);
 font-size: var(--n-option-font-size);
 transition: 
 color .3s var(--n-bezier),
 border-color .3s var(--n-bezier);
 border-top: 1px solid var(--n-action-divider-color);
 color: var(--n-action-text-color);
 `),P("base-select-group-header",`
 position: relative;
 cursor: default;
 padding: var(--n-option-padding);
 color: var(--n-group-header-text-color);
 `),P("base-select-option",`
 cursor: pointer;
 position: relative;
 padding: var(--n-option-padding);
 transition:
 color .3s var(--n-bezier),
 opacity .3s var(--n-bezier);
 box-sizing: border-box;
 color: var(--n-option-text-color);
 opacity: 1;
 `,[le("show-checkmark",`
 padding-right: calc(var(--n-option-padding-right) + 20px);
 `),se("&::before",`
 content: "";
 position: absolute;
 left: 4px;
 right: 4px;
 top: 0;
 bottom: 0;
 border-radius: var(--n-border-radius);
 transition: background-color .3s var(--n-bezier);
 `),se("&:active",`
 color: var(--n-option-text-color-pressed);
 `),le("grouped",`
 padding-left: calc(var(--n-option-padding-left) * 1.5);
 `),le("pending",[se("&::before",`
 background-color: var(--n-option-color-pending);
 `)]),le("selected",`
 color: var(--n-option-text-color-active);
 `,[se("&::before",`
 background-color: var(--n-option-color-active);
 `),le("pending",[se("&::before",`
 background-color: var(--n-option-color-active-pending);
 `)])]),le("disabled",`
 cursor: not-allowed;
 `,[on("selected",`
 color: var(--n-option-text-color-disabled);
 `),le("selected",`
 opacity: var(--n-option-opacity-disabled);
 `)]),V("check",`
 font-size: 16px;
 position: absolute;
 right: calc(var(--n-option-padding-right) - 4px);
 top: calc(50% - 7px);
 color: var(--n-option-check-color);
 transition: color .3s var(--n-bezier);
 `,[Rn({enterScale:"0.5"})])])]),Ht=fe({name:"InternalSelectMenu",props:Object.assign(Object.assign({},Te.props),{clsPrefix:{type:String,required:!0},scrollable:{type:Boolean,default:!0},treeMate:{type:Object,required:!0},multiple:Boolean,size:{type:String,default:"medium"},value:{type:[String,Number,Array],default:null},autoPending:Boolean,virtualScroll:{type:Boolean,default:!0},show:{type:Boolean,default:!0},labelField:{type:String,default:"label"},valueField:{type:String,default:"value"},loading:Boolean,focusable:Boolean,renderLabel:Function,renderOption:Function,nodeProps:Function,showCheckmark:{type:Boolean,default:!0},onMousedown:Function,onScroll:Function,onFocus:Function,onBlur:Function,onKeyup:Function,onKeydown:Function,onTabOut:Function,onMouseenter:Function,onMouseleave:Function,onResize:Function,resetMenuOnOptionsChange:{type:Boolean,default:!0},inlineThemeDisabled:Boolean,onToggle:Function}),setup(e){const{mergedClsPrefixRef:t,mergedRtlRef:l}=sn(e),o=Fn("InternalSelectMenu",l,t),i=Te("InternalSelectMenu","-internal-select-menu",jt,ct,e,Y(e,"clsPrefix")),f=S(null),v=S(null),d=S(null),p=_(()=>e.treeMate.getFlattenedNodes()),m=_(()=>Pt(p.value)),w=S(null);function B(){const{treeMate:s}=e;let h=null;const{value:E}=e;E===null?h=s.getFirstAvailableNode():(e.multiple?h=s.getNode((E||[])[(E||[]).length-1]):h=s.getNode(E),(!h||h.disabled)&&(h=s.getFirstAvailableNode())),b(h||null)}function R(){const{value:s}=w;s&&!e.treeMate.getNode(s.key)&&(w.value=null)}let x;ye(()=>e.show,s=>{s?x=ye(()=>e.treeMate,()=>{e.resetMenuOnOptionsChange?(e.autoPending?B():R(),Tn(k)):R()},{immediate:!0}):x==null||x()},{immediate:!0}),Cn(()=>{x==null||x()});const C=_(()=>ln(i.value.self[me("optionHeight",e.size)])),j=_(()=>$e(i.value.self[me("padding",e.size)])),q=_(()=>e.multiple&&Array.isArray(e.value)?new Set(e.value):new Set),I=_(()=>{const s=p.value;return s&&s.length===0});function T(s){const{onToggle:h}=e;h&&h(s)}function N(s){const{onScroll:h}=e;h&&h(s)}function W(s){var h;(h=d.value)===null||h===void 0||h.sync(),N(s)}function H(){var s;(s=d.value)===null||s===void 0||s.sync()}function Z(){const{value:s}=w;return s||null}function J(s,h){h.disabled||b(h,!1)}function de(s,h){h.disabled||T(h)}function ue(s){var h;Ee(s,"action")||(h=e.onKeyup)===null||h===void 0||h.call(e,s)}function oe(s){var h;Ee(s,"action")||(h=e.onKeydown)===null||h===void 0||h.call(e,s)}function ee(s){var h;(h=e.onMousedown)===null||h===void 0||h.call(e,s),!e.focusable&&s.preventDefault()}function ie(){const{value:s}=w;s&&b(s.getNext({loop:!0}),!0)}function a(){const{value:s}=w;s&&b(s.getPrev({loop:!0}),!0)}function b(s,h=!1){w.value=s,h&&k()}function k(){var s,h;const E=w.value;if(!E)return;const ne=m.value(E.key);ne!==null&&(e.virtualScroll?(s=v.value)===null||s===void 0||s.scrollTo({index:ne}):(h=d.value)===null||h===void 0||h.scrollTo({index:ne,elSize:C.value}))}function A(s){var h,E;!((h=f.value)===null||h===void 0)&&h.contains(s.target)&&((E=e.onFocus)===null||E===void 0||E.call(e,s))}function K(s){var h,E;!((h=f.value)===null||h===void 0)&&h.contains(s.relatedTarget)||(E=e.onBlur)===null||E===void 0||E.call(e,s)}tn(un,{handleOptionMouseEnter:J,handleOptionClick:de,valueSetRef:q,pendingTmNodeRef:w,nodePropsRef:Y(e,"nodeProps"),showCheckmarkRef:Y(e,"showCheckmark"),multipleRef:Y(e,"multiple"),valueRef:Y(e,"value"),renderLabelRef:Y(e,"renderLabel"),renderOptionRef:Y(e,"renderOption"),labelFieldRef:Y(e,"labelField"),valueFieldRef:Y(e,"valueField")}),tn(St,f),Ke(()=>{const{value:s}=d;s&&s.sync()});const L=_(()=>{const{size:s}=e,{common:{cubicBezierEaseInOut:h},self:{height:E,borderRadius:ne,color:xe,groupHeaderTextColor:Ce,actionDividerColor:ce,optionTextColorPressed:te,optionTextColor:Se,optionTextColorDisabled:he,optionTextColorActive:Oe,optionOpacityDisabled:Me,optionCheckColor:Ie,actionTextColor:ke,optionColorPending:ge,optionColorActive:be,loadingColor:ze,loadingSize:Pe,optionColorActivePending:_e,[me("optionFontSize",s)]:Re,[me("optionHeight",s)]:pe,[me("optionPadding",s)]:Q}}=i.value;return{"--n-height":E,"--n-action-divider-color":ce,"--n-action-text-color":ke,"--n-bezier":h,"--n-border-radius":ne,"--n-color":xe,"--n-option-font-size":Re,"--n-group-header-text-color":Ce,"--n-option-check-color":Ie,"--n-option-color-pending":ge,"--n-option-color-active":be,"--n-option-color-active-pending":_e,"--n-option-height":pe,"--n-option-opacity-disabled":Me,"--n-option-text-color":Se,"--n-option-text-color-active":Oe,"--n-option-text-color-disabled":he,"--n-option-text-color-pressed":te,"--n-option-padding":Q,"--n-option-padding-left":$e(Q,"left"),"--n-option-padding-right":$e(Q,"right"),"--n-loading-color":ze,"--n-loading-size":Pe}}),{inlineThemeDisabled:D}=e,$=D?dn("internal-select-menu",_(()=>e.size[0]),L,e):void 0,G={selfRef:f,next:ie,prev:a,getPendingTmNode:Z};return In(f,e.onResize),Object.assign({mergedTheme:i,mergedClsPrefix:t,rtlEnabled:o,virtualListRef:v,scrollbarRef:d,itemSize:C,padding:j,flattenedNodes:p,empty:I,virtualListContainer(){const{value:s}=v;return s==null?void 0:s.listElRef},virtualListContent(){const{value:s}=v;return s==null?void 0:s.itemsElRef},doScroll:N,handleFocusin:A,handleFocusout:K,handleKeyUp:ue,handleKeyDown:oe,handleMouseDown:ee,handleVirtualListResize:H,handleVirtualListScroll:W,cssVars:D?void 0:L,themeClass:$==null?void 0:$.themeClass,onRender:$==null?void 0:$.onRender},G)},render(){const{$slots:e,virtualScroll:t,clsPrefix:l,mergedTheme:o,themeClass:i,onRender:f}=this;return f==null||f(),u("div",{ref:"selfRef",tabindex:this.focusable?0:-1,class:[`${l}-base-select-menu`,this.rtlEnabled&&`${l}-base-select-menu--rtl`,i,this.multiple&&`${l}-base-select-menu--multiple`],style:this.cssVars,onFocusin:this.handleFocusin,onFocusout:this.handleFocusout,onKeyup:this.handleKeyUp,onKeydown:this.handleKeyDown,onMousedown:this.handleMouseDown,onMouseenter:this.onMouseenter,onMouseleave:this.onMouseleave},fn(e.header,v=>v&&u("div",{class:`${l}-base-select-menu__header`,"data-header":!0,key:"header"},v)),this.loading?u("div",{class:`${l}-base-select-menu__loading`},u(st,{clsPrefix:l,strokeWidth:20})):this.empty?u("div",{class:`${l}-base-select-menu__empty`,"data-empty":!0},ut(e.empty,()=>[u(Bt,{theme:o.peers.Empty,themeOverrides:o.peerOverrides.Empty,size:this.size})])):u(dt,{ref:"scrollbarRef",theme:o.peers.Scrollbar,themeOverrides:o.peerOverrides.Scrollbar,scrollable:this.scrollable,container:t?this.virtualListContainer:void 0,content:t?this.virtualListContent:void 0,onScroll:t?void 0:this.doScroll},{default:()=>t?u(Lt,{ref:"virtualListRef",class:`${l}-virtual-list`,items:this.flattenedNodes,itemSize:this.itemSize,showScrollbar:!1,paddingTop:this.padding.top,paddingBottom:this.padding.bottom,onResize:this.handleVirtualListResize,onScroll:this.handleVirtualListScroll,itemResizable:!0},{default:({item:v})=>v.isGroup?u(yn,{key:v.key,clsPrefix:l,tmNode:v}):v.ignored?null:u(xn,{clsPrefix:l,key:v.key,tmNode:v})}):u("div",{class:`${l}-base-select-menu-option-wrapper`,style:{paddingTop:this.padding.top,paddingBottom:this.padding.bottom}},this.flattenedNodes.map(v=>v.isGroup?u(yn,{key:v.key,clsPrefix:l,tmNode:v}):u(xn,{clsPrefix:l,key:v.key,tmNode:v})))}),fn(e.action,v=>v&&[u("div",{class:`${l}-base-select-menu__action`,"data-action":!0,key:"action"},v),u(Vt,{onFocus:this.onTabOut,key:"focus-detector"})]))}}),Kt=se([P("base-selection",`
 --n-padding-single: var(--n-padding-single-top) var(--n-padding-single-right) var(--n-padding-single-bottom) var(--n-padding-single-left);
 --n-padding-multiple: var(--n-padding-multiple-top) var(--n-padding-multiple-right) var(--n-padding-multiple-bottom) var(--n-padding-multiple-left);
 position: relative;
 z-index: auto;
 box-shadow: none;
 width: 100%;
 max-width: 100%;
 display: inline-block;
 vertical-align: bottom;
 border-radius: var(--n-border-radius);
 min-height: var(--n-height);
 line-height: 1.5;
 font-size: var(--n-font-size);
 `,[P("base-loading",`
 color: var(--n-loading-color);
 `),P("base-selection-tags","min-height: var(--n-height);"),V("border, state-border",`
 position: absolute;
 left: 0;
 right: 0;
 top: 0;
 bottom: 0;
 pointer-events: none;
 border: var(--n-border);
 border-radius: inherit;
 transition:
 box-shadow .3s var(--n-bezier),
 border-color .3s var(--n-bezier);
 `),V("state-border",`
 z-index: 1;
 border-color: #0000;
 `),P("base-suffix",`
 cursor: pointer;
 position: absolute;
 top: 50%;
 transform: translateY(-50%);
 right: 10px;
 `,[V("arrow",`
 font-size: var(--n-arrow-size);
 color: var(--n-arrow-color);
 transition: color .3s var(--n-bezier);
 `)]),P("base-selection-overlay",`
 display: flex;
 align-items: center;
 white-space: nowrap;
 pointer-events: none;
 position: absolute;
 top: 0;
 right: 0;
 bottom: 0;
 left: 0;
 padding: var(--n-padding-single);
 transition: color .3s var(--n-bezier);
 `,[V("wrapper",`
 flex-basis: 0;
 flex-grow: 1;
 overflow: hidden;
 text-overflow: ellipsis;
 `)]),P("base-selection-placeholder",`
 color: var(--n-placeholder-color);
 `,[V("inner",`
 max-width: 100%;
 overflow: hidden;
 `)]),P("base-selection-tags",`
 cursor: pointer;
 outline: none;
 box-sizing: border-box;
 position: relative;
 z-index: auto;
 display: flex;
 padding: var(--n-padding-multiple);
 flex-wrap: wrap;
 align-items: center;
 width: 100%;
 vertical-align: bottom;
 background-color: var(--n-color);
 border-radius: inherit;
 transition:
 color .3s var(--n-bezier),
 box-shadow .3s var(--n-bezier),
 background-color .3s var(--n-bezier);
 `),P("base-selection-label",`
 height: var(--n-height);
 display: inline-flex;
 width: 100%;
 vertical-align: bottom;
 cursor: pointer;
 outline: none;
 z-index: auto;
 box-sizing: border-box;
 position: relative;
 transition:
 color .3s var(--n-bezier),
 box-shadow .3s var(--n-bezier),
 background-color .3s var(--n-bezier);
 border-radius: inherit;
 background-color: var(--n-color);
 align-items: center;
 `,[P("base-selection-input",`
 font-size: inherit;
 line-height: inherit;
 outline: none;
 cursor: pointer;
 box-sizing: border-box;
 border:none;
 width: 100%;
 padding: var(--n-padding-single);
 background-color: #0000;
 color: var(--n-text-color);
 transition: color .3s var(--n-bezier);
 caret-color: var(--n-caret-color);
 `,[V("content",`
 text-overflow: ellipsis;
 overflow: hidden;
 white-space: nowrap; 
 `)]),V("render-label",`
 color: var(--n-text-color);
 `)]),on("disabled",[se("&:hover",[V("state-border",`
 box-shadow: var(--n-box-shadow-hover);
 border: var(--n-border-hover);
 `)]),le("focus",[V("state-border",`
 box-shadow: var(--n-box-shadow-focus);
 border: var(--n-border-focus);
 `)]),le("active",[V("state-border",`
 box-shadow: var(--n-box-shadow-active);
 border: var(--n-border-active);
 `),P("base-selection-label","background-color: var(--n-color-active);"),P("base-selection-tags","background-color: var(--n-color-active);")])]),le("disabled","cursor: not-allowed;",[V("arrow",`
 color: var(--n-arrow-color-disabled);
 `),P("base-selection-label",`
 cursor: not-allowed;
 background-color: var(--n-color-disabled);
 `,[P("base-selection-input",`
 cursor: not-allowed;
 color: var(--n-text-color-disabled);
 `),V("render-label",`
 color: var(--n-text-color-disabled);
 `)]),P("base-selection-tags",`
 cursor: not-allowed;
 background-color: var(--n-color-disabled);
 `),P("base-selection-placeholder",`
 cursor: not-allowed;
 color: var(--n-placeholder-color-disabled);
 `)]),P("base-selection-input-tag",`
 height: calc(var(--n-height) - 6px);
 line-height: calc(var(--n-height) - 6px);
 outline: none;
 display: none;
 position: relative;
 margin-bottom: 3px;
 max-width: 100%;
 vertical-align: bottom;
 `,[V("input",`
 font-size: inherit;
 font-family: inherit;
 min-width: 1px;
 padding: 0;
 background-color: #0000;
 outline: none;
 border: none;
 max-width: 100%;
 overflow: hidden;
 width: 1em;
 line-height: inherit;
 cursor: pointer;
 color: var(--n-text-color);
 caret-color: var(--n-caret-color);
 `),V("mirror",`
 position: absolute;
 left: 0;
 top: 0;
 white-space: pre;
 visibility: hidden;
 user-select: none;
 -webkit-user-select: none;
 opacity: 0;
 `)]),["warning","error"].map(e=>le(`${e}-status`,[V("state-border",`border: var(--n-border-${e});`),on("disabled",[se("&:hover",[V("state-border",`
 box-shadow: var(--n-box-shadow-hover-${e});
 border: var(--n-border-hover-${e});
 `)]),le("active",[V("state-border",`
 box-shadow: var(--n-box-shadow-active-${e});
 border: var(--n-border-active-${e});
 `),P("base-selection-label",`background-color: var(--n-color-active-${e});`),P("base-selection-tags",`background-color: var(--n-color-active-${e});`)]),le("focus",[V("state-border",`
 box-shadow: var(--n-box-shadow-focus-${e});
 border: var(--n-border-focus-${e});
 `)])])]))]),P("base-selection-popover",`
 margin-bottom: -3px;
 display: flex;
 flex-wrap: wrap;
 margin-right: -8px;
 `),P("base-selection-tag-wrapper",`
 max-width: 100%;
 display: inline-flex;
 padding: 0 7px 3px 0;
 `,[se("&:last-child","padding-right: 0;"),P("tag",`
 font-size: 14px;
 max-width: 100%;
 `,[V("content",`
 line-height: 1.25;
 text-overflow: ellipsis;
 overflow: hidden;
 `)])])]),Ut=fe({name:"InternalSelection",props:Object.assign(Object.assign({},Te.props),{clsPrefix:{type:String,required:!0},bordered:{type:Boolean,default:void 0},active:Boolean,pattern:{type:String,default:""},placeholder:String,selectedOption:{type:Object,default:null},selectedOptions:{type:Array,default:null},labelField:{type:String,default:"label"},valueField:{type:String,default:"value"},multiple:Boolean,filterable:Boolean,clearable:Boolean,disabled:Boolean,size:{type:String,default:"medium"},loading:Boolean,autofocus:Boolean,showArrow:{type:Boolean,default:!0},inputProps:Object,focused:Boolean,renderTag:Function,onKeydown:Function,onClick:Function,onBlur:Function,onFocus:Function,onDeleteOption:Function,maxTagCount:[String,Number],ellipsisTagPopoverProps:Object,onClear:Function,onPatternInput:Function,onPatternFocus:Function,onPatternBlur:Function,renderLabel:Function,status:String,inlineThemeDisabled:Boolean,ignoreComposition:{type:Boolean,default:!0},onResize:Function}),setup(e){const{mergedClsPrefixRef:t,mergedRtlRef:l}=sn(e),o=Fn("InternalSelection",l,t),i=S(null),f=S(null),v=S(null),d=S(null),p=S(null),m=S(null),w=S(null),B=S(null),R=S(null),x=S(null),C=S(!1),j=S(!1),q=S(!1),I=Te("InternalSelection","-internal-selection",Kt,vt,e,Y(e,"clsPrefix")),T=_(()=>e.clearable&&!e.disabled&&(q.value||e.active)),N=_(()=>e.selectedOption?e.renderTag?e.renderTag({option:e.selectedOption,handleClose:()=>{}}):e.renderLabel?e.renderLabel(e.selectedOption,!0):Fe(e.selectedOption[e.labelField],e.selectedOption,!0):e.placeholder),W=_(()=>{const r=e.selectedOption;if(r)return r[e.labelField]}),H=_(()=>e.multiple?!!(Array.isArray(e.selectedOptions)&&e.selectedOptions.length):e.selectedOption!==null);function Z(){var r;const{value:g}=i;if(g){const{value:U}=f;U&&(U.style.width=`${g.offsetWidth}px`,e.maxTagCount!=="responsive"&&((r=R.value)===null||r===void 0||r.sync({showAllItemsBeforeCalculate:!1})))}}function J(){const{value:r}=x;r&&(r.style.display="none")}function de(){const{value:r}=x;r&&(r.style.display="inline-block")}ye(Y(e,"active"),r=>{r||J()}),ye(Y(e,"pattern"),()=>{e.multiple&&Tn(Z)});function ue(r){const{onFocus:g}=e;g&&g(r)}function oe(r){const{onBlur:g}=e;g&&g(r)}function ee(r){const{onDeleteOption:g}=e;g&&g(r)}function ie(r){const{onClear:g}=e;g&&g(r)}function a(r){const{onPatternInput:g}=e;g&&g(r)}function b(r){var g;(!r.relatedTarget||!(!((g=v.value)===null||g===void 0)&&g.contains(r.relatedTarget)))&&ue(r)}function k(r){var g;!((g=v.value)===null||g===void 0)&&g.contains(r.relatedTarget)||oe(r)}function A(r){ie(r)}function K(){q.value=!0}function L(){q.value=!1}function D(r){!e.active||!e.filterable||r.target!==f.value&&r.preventDefault()}function $(r){ee(r)}const G=S(!1);function s(r){if(r.key==="Backspace"&&!G.value&&!e.pattern.length){const{selectedOptions:g}=e;g!=null&&g.length&&$(g[g.length-1])}}let h=null;function E(r){const{value:g}=i;if(g){const U=r.target.value;g.textContent=U,Z()}e.ignoreComposition&&G.value?h=r:a(r)}function ne(){G.value=!0}function xe(){G.value=!1,e.ignoreComposition&&a(h),h=null}function Ce(r){var g;j.value=!0,(g=e.onPatternFocus)===null||g===void 0||g.call(e,r)}function ce(r){var g;j.value=!1,(g=e.onPatternBlur)===null||g===void 0||g.call(e,r)}function te(){var r,g;if(e.filterable)j.value=!1,(r=m.value)===null||r===void 0||r.blur(),(g=f.value)===null||g===void 0||g.blur();else if(e.multiple){const{value:U}=d;U==null||U.blur()}else{const{value:U}=p;U==null||U.blur()}}function Se(){var r,g,U;e.filterable?(j.value=!1,(r=m.value)===null||r===void 0||r.focus()):e.multiple?(g=d.value)===null||g===void 0||g.focus():(U=p.value)===null||U===void 0||U.focus()}function he(){const{value:r}=f;r&&(de(),r.focus())}function Oe(){const{value:r}=f;r&&r.blur()}function Me(r){const{value:g}=w;g&&g.setTextContent(`+${r}`)}function Ie(){const{value:r}=B;return r}function ke(){return f.value}let ge=null;function be(){ge!==null&&window.clearTimeout(ge)}function ze(){e.active||(be(),ge=window.setTimeout(()=>{H.value&&(C.value=!0)},100))}function Pe(){be()}function _e(r){r||(be(),C.value=!1)}ye(H,r=>{r||(C.value=!1)}),Ke(()=>{gt(()=>{const r=m.value;r&&(e.disabled?r.removeAttribute("tabindex"):r.tabIndex=j.value?-1:0)})}),In(v,e.onResize);const{inlineThemeDisabled:Re}=e,pe=_(()=>{const{size:r}=e,{common:{cubicBezierEaseInOut:g},self:{fontWeight:U,borderRadius:Ue,color:qe,placeholderColor:Ae,textColor:Ne,paddingSingle:Le,paddingMultiple:Ge,caretColor:Xe,colorDisabled:De,textColorDisabled:ve,placeholderColorDisabled:n,colorActive:c,boxShadowFocus:y,boxShadowActive:z,boxShadowHover:O,border:F,borderFocus:M,borderHover:X,borderActive:re,arrowColor:zn,arrowColorDisabled:Pn,loadingColor:_n,colorActiveWarning:Bn,boxShadowFocusWarning:$n,boxShadowActiveWarning:En,boxShadowHoverWarning:An,borderWarning:Nn,borderFocusWarning:Ln,borderHoverWarning:Dn,borderActiveWarning:Vn,colorActiveError:Wn,boxShadowFocusError:jn,boxShadowActiveError:Hn,boxShadowHoverError:Kn,borderError:Un,borderFocusError:qn,borderHoverError:Gn,borderActiveError:Xn,clearColor:Yn,clearColorHover:Jn,clearColorPressed:Qn,clearSize:Zn,arrowSize:et,[me("height",r)]:nt,[me("fontSize",r)]:tt}}=I.value,Ve=$e(Le),We=$e(Ge);return{"--n-bezier":g,"--n-border":F,"--n-border-active":re,"--n-border-focus":M,"--n-border-hover":X,"--n-border-radius":Ue,"--n-box-shadow-active":z,"--n-box-shadow-focus":y,"--n-box-shadow-hover":O,"--n-caret-color":Xe,"--n-color":qe,"--n-color-active":c,"--n-color-disabled":De,"--n-font-size":tt,"--n-height":nt,"--n-padding-single-top":Ve.top,"--n-padding-multiple-top":We.top,"--n-padding-single-right":Ve.right,"--n-padding-multiple-right":We.right,"--n-padding-single-left":Ve.left,"--n-padding-multiple-left":We.left,"--n-padding-single-bottom":Ve.bottom,"--n-padding-multiple-bottom":We.bottom,"--n-placeholder-color":Ae,"--n-placeholder-color-disabled":n,"--n-text-color":Ne,"--n-text-color-disabled":ve,"--n-arrow-color":zn,"--n-arrow-color-disabled":Pn,"--n-loading-color":_n,"--n-color-active-warning":Bn,"--n-box-shadow-focus-warning":$n,"--n-box-shadow-active-warning":En,"--n-box-shadow-hover-warning":An,"--n-border-warning":Nn,"--n-border-focus-warning":Ln,"--n-border-hover-warning":Dn,"--n-border-active-warning":Vn,"--n-color-active-error":Wn,"--n-box-shadow-focus-error":jn,"--n-box-shadow-active-error":Hn,"--n-box-shadow-hover-error":Kn,"--n-border-error":Un,"--n-border-focus-error":qn,"--n-border-hover-error":Gn,"--n-border-active-error":Xn,"--n-clear-size":Zn,"--n-clear-color":Yn,"--n-clear-color-hover":Jn,"--n-clear-color-pressed":Qn,"--n-arrow-size":et,"--n-font-weight":U}}),Q=Re?dn("internal-selection",_(()=>e.size[0]),pe,e):void 0;return{mergedTheme:I,mergedClearable:T,mergedClsPrefix:t,rtlEnabled:o,patternInputFocused:j,filterablePlaceholder:N,label:W,selected:H,showTagsPanel:C,isComposing:G,counterRef:w,counterWrapperRef:B,patternInputMirrorRef:i,patternInputRef:f,selfRef:v,multipleElRef:d,singleElRef:p,patternInputWrapperRef:m,overflowRef:R,inputTagElRef:x,handleMouseDown:D,handleFocusin:b,handleClear:A,handleMouseEnter:K,handleMouseLeave:L,handleDeleteOption:$,handlePatternKeyDown:s,handlePatternInputInput:E,handlePatternInputBlur:ce,handlePatternInputFocus:Ce,handleMouseEnterCounter:ze,handleMouseLeaveCounter:Pe,handleFocusout:k,handleCompositionEnd:xe,handleCompositionStart:ne,onPopoverUpdateShow:_e,focus:Se,focusInput:he,blur:te,blurInput:Oe,updateCounter:Me,getCounter:Ie,getTail:ke,renderLabel:e.renderLabel,cssVars:Re?void 0:pe,themeClass:Q==null?void 0:Q.themeClass,onRender:Q==null?void 0:Q.onRender}},render(){const{status:e,multiple:t,size:l,disabled:o,filterable:i,maxTagCount:f,bordered:v,clsPrefix:d,ellipsisTagPopoverProps:p,onRender:m,renderTag:w,renderLabel:B}=this;m==null||m();const R=f==="responsive",x=typeof f=="number",C=R||x,j=u(ft,null,{default:()=>u(Ot,{clsPrefix:d,loading:this.loading,showArrow:this.showArrow,showClear:this.mergedClearable&&this.selected,onClear:this.handleClear},{default:()=>{var I,T;return(T=(I=this.$slots).arrow)===null||T===void 0?void 0:T.call(I)}})});let q;if(t){const{labelField:I}=this,T=a=>u("div",{class:`${d}-base-selection-tag-wrapper`,key:a.value},w?w({option:a,handleClose:()=>{this.handleDeleteOption(a)}}):u(Je,{size:l,closable:!a.disabled,disabled:o,onClose:()=>{this.handleDeleteOption(a)},internalCloseIsButtonTag:!1,internalCloseFocusable:!1},{default:()=>B?B(a,!0):Fe(a[I],a,!0)})),N=()=>(x?this.selectedOptions.slice(0,f):this.selectedOptions).map(T),W=i?u("div",{class:`${d}-base-selection-input-tag`,ref:"inputTagElRef",key:"__input-tag__"},u("input",Object.assign({},this.inputProps,{ref:"patternInputRef",tabindex:-1,disabled:o,value:this.pattern,autofocus:this.autofocus,class:`${d}-base-selection-input-tag__input`,onBlur:this.handlePatternInputBlur,onFocus:this.handlePatternInputFocus,onKeydown:this.handlePatternKeyDown,onInput:this.handlePatternInputInput,onCompositionstart:this.handleCompositionStart,onCompositionend:this.handleCompositionEnd})),u("span",{ref:"patternInputMirrorRef",class:`${d}-base-selection-input-tag__mirror`},this.pattern)):null,H=R?()=>u("div",{class:`${d}-base-selection-tag-wrapper`,ref:"counterWrapperRef"},u(Je,{size:l,ref:"counterRef",onMouseenter:this.handleMouseEnterCounter,onMouseleave:this.handleMouseLeaveCounter,disabled:o})):void 0;let Z;if(x){const a=this.selectedOptions.length-f;a>0&&(Z=u("div",{class:`${d}-base-selection-tag-wrapper`,key:"__counter__"},u(Je,{size:l,ref:"counterRef",onMouseenter:this.handleMouseEnterCounter,disabled:o},{default:()=>`+${a}`})))}const J=R?i?u(vn,{ref:"overflowRef",updateCounter:this.updateCounter,getCounter:this.getCounter,getTail:this.getTail,style:{width:"100%",display:"flex",overflow:"hidden"}},{default:N,counter:H,tail:()=>W}):u(vn,{ref:"overflowRef",updateCounter:this.updateCounter,getCounter:this.getCounter,style:{width:"100%",display:"flex",overflow:"hidden"}},{default:N,counter:H}):x&&Z?N().concat(Z):N(),de=C?()=>u("div",{class:`${d}-base-selection-popover`},R?N():this.selectedOptions.map(T)):void 0,ue=C?Object.assign({show:this.showTagsPanel,trigger:"hover",overlap:!0,placement:"top",width:"trigger",onUpdateShow:this.onPopoverUpdateShow,theme:this.mergedTheme.peers.Popover,themeOverrides:this.mergedTheme.peerOverrides.Popover},p):null,ee=(this.selected?!1:this.active?!this.pattern&&!this.isComposing:!0)?u("div",{class:`${d}-base-selection-placeholder ${d}-base-selection-overlay`},u("div",{class:`${d}-base-selection-placeholder__inner`},this.placeholder)):null,ie=i?u("div",{ref:"patternInputWrapperRef",class:`${d}-base-selection-tags`},J,R?null:W,j):u("div",{ref:"multipleElRef",class:`${d}-base-selection-tags`,tabindex:o?void 0:0},J,j);q=u(ht,null,C?u(It,Object.assign({},ue,{scrollable:!0,style:"max-height: calc(var(--v-target-height) * 6.6);"}),{trigger:()=>ie,default:de}):ie,ee)}else if(i){const I=this.pattern||this.isComposing,T=this.active?!I:!this.selected,N=this.active?!1:this.selected;q=u("div",{ref:"patternInputWrapperRef",class:`${d}-base-selection-label`,title:this.patternInputFocused?void 0:wn(this.label)},u("input",Object.assign({},this.inputProps,{ref:"patternInputRef",class:`${d}-base-selection-input`,value:this.active?this.pattern:"",placeholder:"",readonly:o,disabled:o,tabindex:-1,autofocus:this.autofocus,onFocus:this.handlePatternInputFocus,onBlur:this.handlePatternInputBlur,onInput:this.handlePatternInputInput,onCompositionstart:this.handleCompositionStart,onCompositionend:this.handleCompositionEnd})),N?u("div",{class:`${d}-base-selection-label__render-label ${d}-base-selection-overlay`,key:"input"},u("div",{class:`${d}-base-selection-overlay__wrapper`},w?w({option:this.selectedOption,handleClose:()=>{}}):B?B(this.selectedOption,!0):Fe(this.label,this.selectedOption,!0))):null,T?u("div",{class:`${d}-base-selection-placeholder ${d}-base-selection-overlay`,key:"placeholder"},u("div",{class:`${d}-base-selection-overlay__wrapper`},this.filterablePlaceholder)):null,j)}else q=u("div",{ref:"singleElRef",class:`${d}-base-selection-label`,tabindex:this.disabled?void 0:0},this.label!==void 0?u("div",{class:`${d}-base-selection-input`,title:wn(this.label),key:"input"},u("div",{class:`${d}-base-selection-input__content`},w?w({option:this.selectedOption,handleClose:()=>{}}):B?B(this.selectedOption,!0):Fe(this.label,this.selectedOption,!0))):u("div",{class:`${d}-base-selection-placeholder ${d}-base-selection-overlay`,key:"placeholder"},u("div",{class:`${d}-base-selection-placeholder__inner`},this.placeholder)),j);return u("div",{ref:"selfRef",class:[`${d}-base-selection`,this.rtlEnabled&&`${d}-base-selection--rtl`,this.themeClass,e&&`${d}-base-selection--${e}-status`,{[`${d}-base-selection--active`]:this.active,[`${d}-base-selection--selected`]:this.selected||this.active&&this.pattern,[`${d}-base-selection--disabled`]:this.disabled,[`${d}-base-selection--multiple`]:this.multiple,[`${d}-base-selection--focus`]:this.focused}],style:this.cssVars,onClick:this.onClick,onMouseenter:this.handleMouseEnter,onMouseleave:this.handleMouseLeave,onKeydown:this.onKeydown,onFocusin:this.handleFocusin,onFocusout:this.handleFocusout,onMousedown:this.handleMouseDown},q,v?u("div",{class:`${d}-base-selection__border`}):null,v?u("div",{class:`${d}-base-selection__state-border`}):null)}});function He(e){return e.type==="group"}function kn(e){return e.type==="ignored"}function nn(e,t){try{return!!(1+t.toString().toLowerCase().indexOf(e.trim().toLowerCase()))}catch{return!1}}function qt(e,t){return{getIsGroup:He,getIgnored:kn,getKey(o){return He(o)?o.name||o.key||"key-required":o[e]},getChildren(o){return o[t]}}}function Gt(e,t,l,o){if(!t)return e;function i(f){if(!Array.isArray(f))return[];const v=[];for(const d of f)if(He(d)){const p=i(d[o]);p.length&&v.push(Object.assign({},d,{[o]:p}))}else{if(kn(d))continue;t(l,d)&&v.push(d)}return v}return i(e)}function Xt(e,t,l){const o=new Map;return e.forEach(i=>{He(i)?i[l].forEach(f=>{o.set(f[t],f)}):o.set(i[t],i)}),o}const Yt=se([P("select",`
 z-index: auto;
 outline: none;
 width: 100%;
 position: relative;
 font-weight: var(--n-font-weight);
 `),P("select-menu",`
 margin: 4px 0;
 box-shadow: var(--n-menu-box-shadow);
 `,[Rn({originalTransition:"background-color .3s var(--n-bezier), box-shadow .3s var(--n-bezier)"})])]),Jt=Object.assign(Object.assign({},Te.props),{to:rn.propTo,bordered:{type:Boolean,default:void 0},clearable:Boolean,clearFilterAfterSelect:{type:Boolean,default:!0},options:{type:Array,default:()=>[]},defaultValue:{type:[String,Number,Array],default:null},keyboard:{type:Boolean,default:!0},value:[String,Number,Array],placeholder:String,menuProps:Object,multiple:Boolean,size:String,menuSize:{type:String},filterable:Boolean,disabled:{type:Boolean,default:void 0},remote:Boolean,loading:Boolean,filter:Function,placement:{type:String,default:"bottom-start"},widthMode:{type:String,default:"trigger"},tag:Boolean,onCreate:Function,fallbackOption:{type:[Function,Boolean],default:void 0},show:{type:Boolean,default:void 0},showArrow:{type:Boolean,default:!0},maxTagCount:[Number,String],ellipsisTagPopoverProps:Object,consistentMenuWidth:{type:Boolean,default:!0},virtualScroll:{type:Boolean,default:!0},labelField:{type:String,default:"label"},valueField:{type:String,default:"value"},childrenField:{type:String,default:"children"},renderLabel:Function,renderOption:Function,renderTag:Function,"onUpdate:value":[Function,Array],inputProps:Object,nodeProps:Function,ignoreComposition:{type:Boolean,default:!0},showOnFocus:Boolean,onUpdateValue:[Function,Array],onBlur:[Function,Array],onClear:[Function,Array],onFocus:[Function,Array],onScroll:[Function,Array],onSearch:[Function,Array],onUpdateShow:[Function,Array],"onUpdate:show":[Function,Array],displayDirective:{type:String,default:"show"},resetMenuOnOptionsChange:{type:Boolean,default:!0},status:String,showCheckmark:{type:Boolean,default:!0},onChange:[Function,Array],items:Array}),ul=fe({name:"Select",props:Jt,slots:Object,setup(e){const{mergedClsPrefixRef:t,mergedBorderedRef:l,namespaceRef:o,inlineThemeDisabled:i}=sn(e),f=Te("Select","-select",Yt,mt,e,t),v=S(e.defaultValue),d=Y(e,"value"),p=gn(d,v),m=S(!1),w=S(""),B=$t(e,["items","options"]),R=S([]),x=S([]),C=_(()=>x.value.concat(R.value).concat(B.value)),j=_(()=>{const{filter:n}=e;if(n)return n;const{labelField:c,valueField:y}=e;return(z,O)=>{if(!O)return!1;const F=O[c];if(typeof F=="string")return nn(z,F);const M=O[y];return typeof M=="string"?nn(z,M):typeof M=="number"?nn(z,String(M)):!1}}),q=_(()=>{if(e.remote)return B.value;{const{value:n}=C,{value:c}=w;return!c.length||!e.filterable?n:Gt(n,j.value,c,e.childrenField)}}),I=_(()=>{const{valueField:n,childrenField:c}=e,y=qt(n,c);return _t(q.value,y)}),T=_(()=>Xt(C.value,e.valueField,e.childrenField)),N=S(!1),W=gn(Y(e,"show"),N),H=S(null),Z=S(null),J=S(null),{localeRef:de}=Mt("Select"),ue=_(()=>{var n;return(n=e.placeholder)!==null&&n!==void 0?n:de.value.placeholder}),oe=[],ee=S(new Map),ie=_(()=>{const{fallbackOption:n}=e;if(n===void 0){const{labelField:c,valueField:y}=e;return z=>({[c]:String(z),[y]:z})}return n===!1?!1:c=>Object.assign(n(c),{value:c})});function a(n){const c=e.remote,{value:y}=ee,{value:z}=T,{value:O}=ie,F=[];return n.forEach(M=>{if(z.has(M))F.push(z.get(M));else if(c&&y.has(M))F.push(y.get(M));else if(O){const X=O(M);X&&F.push(X)}}),F}const b=_(()=>{if(e.multiple){const{value:n}=p;return Array.isArray(n)?a(n):[]}return null}),k=_(()=>{const{value:n}=p;return!e.multiple&&!Array.isArray(n)?n===null?null:a([n])[0]||null:null}),A=wt(e),{mergedSizeRef:K,mergedDisabledRef:L,mergedStatusRef:D}=A;function $(n,c){const{onChange:y,"onUpdate:value":z,onUpdateValue:O}=e,{nTriggerFormChange:F,nTriggerFormInput:M}=A;y&&ae(y,n,c),O&&ae(O,n,c),z&&ae(z,n,c),v.value=n,F(),M()}function G(n){const{onBlur:c}=e,{nTriggerFormBlur:y}=A;c&&ae(c,n),y()}function s(){const{onClear:n}=e;n&&ae(n)}function h(n){const{onFocus:c,showOnFocus:y}=e,{nTriggerFormFocus:z}=A;c&&ae(c,n),z(),y&&ce()}function E(n){const{onSearch:c}=e;c&&ae(c,n)}function ne(n){const{onScroll:c}=e;c&&ae(c,n)}function xe(){var n;const{remote:c,multiple:y}=e;if(c){const{value:z}=ee;if(y){const{valueField:O}=e;(n=b.value)===null||n===void 0||n.forEach(F=>{z.set(F[O],F)})}else{const O=k.value;O&&z.set(O[e.valueField],O)}}}function Ce(n){const{onUpdateShow:c,"onUpdate:show":y}=e;c&&ae(c,n),y&&ae(y,n),N.value=n}function ce(){L.value||(Ce(!0),N.value=!0,e.filterable&&Le())}function te(){Ce(!1)}function Se(){w.value="",x.value=oe}const he=S(!1);function Oe(){e.filterable&&(he.value=!0)}function Me(){e.filterable&&(he.value=!1,W.value||Se())}function Ie(){L.value||(W.value?e.filterable?Le():te():ce())}function ke(n){var c,y;!((y=(c=J.value)===null||c===void 0?void 0:c.selfRef)===null||y===void 0)&&y.contains(n.relatedTarget)||(m.value=!1,G(n),te())}function ge(n){h(n),m.value=!0}function be(){m.value=!0}function ze(n){var c;!((c=H.value)===null||c===void 0)&&c.$el.contains(n.relatedTarget)||(m.value=!1,G(n),te())}function Pe(){var n;(n=H.value)===null||n===void 0||n.focus(),te()}function _e(n){var c;W.value&&(!((c=H.value)===null||c===void 0)&&c.$el.contains(xt(n))||te())}function Re(n){if(!Array.isArray(n))return[];if(ie.value)return Array.from(n);{const{remote:c}=e,{value:y}=T;if(c){const{value:z}=ee;return n.filter(O=>y.has(O)||z.has(O))}else return n.filter(z=>y.has(z))}}function pe(n){Q(n.rawNode)}function Q(n){if(L.value)return;const{tag:c,remote:y,clearFilterAfterSelect:z,valueField:O}=e;if(c&&!y){const{value:F}=x,M=F[0]||null;if(M){const X=R.value;X.length?X.push(M):R.value=[M],x.value=oe}}if(y&&ee.value.set(n[O],n),e.multiple){const F=Re(p.value),M=F.findIndex(X=>X===n[O]);if(~M){if(F.splice(M,1),c&&!y){const X=r(n[O]);~X&&(R.value.splice(X,1),z&&(w.value=""))}}else F.push(n[O]),z&&(w.value="");$(F,a(F))}else{if(c&&!y){const F=r(n[O]);~F?R.value=[R.value[F]]:R.value=oe}Ne(),te(),$(n[O],n)}}function r(n){return R.value.findIndex(y=>y[e.valueField]===n)}function g(n){W.value||ce();const{value:c}=n.target;w.value=c;const{tag:y,remote:z}=e;if(E(c),y&&!z){if(!c){x.value=oe;return}const{onCreate:O}=e,F=O?O(c):{[e.labelField]:c,[e.valueField]:c},{valueField:M,labelField:X}=e;B.value.some(re=>re[M]===F[M]||re[X]===F[X])||R.value.some(re=>re[M]===F[M]||re[X]===F[X])?x.value=oe:x.value=[F]}}function U(n){n.stopPropagation();const{multiple:c}=e;!c&&e.filterable&&te(),s(),c?$([],[]):$(null,null)}function Ue(n){!Ee(n,"action")&&!Ee(n,"empty")&&!Ee(n,"header")&&n.preventDefault()}function qe(n){ne(n)}function Ae(n){var c,y,z,O,F;if(!e.keyboard){n.preventDefault();return}switch(n.key){case" ":if(e.filterable)break;n.preventDefault();case"Enter":if(!(!((c=H.value)===null||c===void 0)&&c.isComposing)){if(W.value){const M=(y=J.value)===null||y===void 0?void 0:y.getPendingTmNode();M?pe(M):e.filterable||(te(),Ne())}else if(ce(),e.tag&&he.value){const M=x.value[0];if(M){const X=M[e.valueField],{value:re}=p;e.multiple&&Array.isArray(re)&&re.includes(X)||Q(M)}}}n.preventDefault();break;case"ArrowUp":if(n.preventDefault(),e.loading)return;W.value&&((z=J.value)===null||z===void 0||z.prev());break;case"ArrowDown":if(n.preventDefault(),e.loading)return;W.value?(O=J.value)===null||O===void 0||O.next():ce();break;case"Escape":W.value&&(Ct(n),te()),(F=H.value)===null||F===void 0||F.focus();break}}function Ne(){var n;(n=H.value)===null||n===void 0||n.focus()}function Le(){var n;(n=H.value)===null||n===void 0||n.focusInput()}function Ge(){var n;W.value&&((n=Z.value)===null||n===void 0||n.syncPosition())}xe(),ye(Y(e,"options"),xe);const Xe={focus:()=>{var n;(n=H.value)===null||n===void 0||n.focus()},focusInput:()=>{var n;(n=H.value)===null||n===void 0||n.focusInput()},blur:()=>{var n;(n=H.value)===null||n===void 0||n.blur()},blurInput:()=>{var n;(n=H.value)===null||n===void 0||n.blurInput()}},De=_(()=>{const{self:{menuBoxShadow:n}}=f.value;return{"--n-menu-box-shadow":n}}),ve=i?dn("select",void 0,De,e):void 0;return Object.assign(Object.assign({},Xe),{mergedStatus:D,mergedClsPrefix:t,mergedBordered:l,namespace:o,treeMate:I,isMounted:yt(),triggerRef:H,menuRef:J,pattern:w,uncontrolledShow:N,mergedShow:W,adjustedTo:rn(e),uncontrolledValue:v,mergedValue:p,followerRef:Z,localizedPlaceholder:ue,selectedOption:k,selectedOptions:b,mergedSize:K,mergedDisabled:L,focused:m,activeWithoutMenuOpen:he,inlineThemeDisabled:i,onTriggerInputFocus:Oe,onTriggerInputBlur:Me,handleTriggerOrMenuResize:Ge,handleMenuFocus:be,handleMenuBlur:ze,handleMenuTabOut:Pe,handleTriggerClick:Ie,handleToggle:pe,handleDeleteOption:Q,handlePatternInput:g,handleClear:U,handleTriggerBlur:ke,handleTriggerFocus:ge,handleKeydown:Ae,handleMenuAfterLeave:Se,handleMenuClickOutside:_e,handleMenuScroll:qe,handleMenuKeydown:Ae,handleMenuMousedown:Ue,mergedTheme:f,cssVars:i?void 0:De,themeClass:ve==null?void 0:ve.themeClass,onRender:ve==null?void 0:ve.onRender})},render(){return u("div",{class:`${this.mergedClsPrefix}-select`},u(Rt,null,{default:()=>[u(Ft,null,{default:()=>u(Ut,{ref:"triggerRef",inlineThemeDisabled:this.inlineThemeDisabled,status:this.mergedStatus,inputProps:this.inputProps,clsPrefix:this.mergedClsPrefix,showArrow:this.showArrow,maxTagCount:this.maxTagCount,ellipsisTagPopoverProps:this.ellipsisTagPopoverProps,bordered:this.mergedBordered,active:this.activeWithoutMenuOpen||this.mergedShow,pattern:this.pattern,placeholder:this.localizedPlaceholder,selectedOption:this.selectedOption,selectedOptions:this.selectedOptions,multiple:this.multiple,renderTag:this.renderTag,renderLabel:this.renderLabel,filterable:this.filterable,clearable:this.clearable,disabled:this.mergedDisabled,size:this.mergedSize,theme:this.mergedTheme.peers.InternalSelection,labelField:this.labelField,valueField:this.valueField,themeOverrides:this.mergedTheme.peerOverrides.InternalSelection,loading:this.loading,focused:this.focused,onClick:this.handleTriggerClick,onDeleteOption:this.handleDeleteOption,onPatternInput:this.handlePatternInput,onClear:this.handleClear,onBlur:this.handleTriggerBlur,onFocus:this.handleTriggerFocus,onKeydown:this.handleKeydown,onPatternBlur:this.onTriggerInputBlur,onPatternFocus:this.onTriggerInputFocus,onResize:this.handleTriggerOrMenuResize,ignoreComposition:this.ignoreComposition},{arrow:()=>{var e,t;return[(t=(e=this.$slots).arrow)===null||t===void 0?void 0:t.call(e)]}})}),u(Tt,{ref:"followerRef",show:this.mergedShow,to:this.adjustedTo,teleportDisabled:this.adjustedTo===rn.tdkey,containerClass:this.namespace,width:this.consistentMenuWidth?"target":void 0,minWidth:"target",placement:this.placement},{default:()=>u(Sn,{name:"fade-in-scale-up-transition",appear:this.isMounted,onAfterLeave:this.handleMenuAfterLeave},{default:()=>{var e,t,l;return this.mergedShow||this.displayDirective==="show"?((e=this.onRender)===null||e===void 0||e.call(this),bt(u(Ht,Object.assign({},this.menuProps,{ref:"menuRef",onResize:this.handleTriggerOrMenuResize,inlineThemeDisabled:this.inlineThemeDisabled,virtualScroll:this.consistentMenuWidth&&this.virtualScroll,class:[`${this.mergedClsPrefix}-select-menu`,this.themeClass,(t=this.menuProps)===null||t===void 0?void 0:t.class],clsPrefix:this.mergedClsPrefix,focusable:!0,labelField:this.labelField,valueField:this.valueField,autoPending:!0,nodeProps:this.nodeProps,theme:this.mergedTheme.peers.InternalSelectMenu,themeOverrides:this.mergedTheme.peerOverrides.InternalSelectMenu,treeMate:this.treeMate,multiple:this.multiple,size:this.menuSize,renderOption:this.renderOption,renderLabel:this.renderLabel,value:this.mergedValue,style:[(l=this.menuProps)===null||l===void 0?void 0:l.style,this.cssVars],onToggle:this.handleToggle,onScroll:this.handleMenuScroll,onFocus:this.handleMenuFocus,onBlur:this.handleMenuBlur,onKeydown:this.handleMenuKeydown,onTabOut:this.handleMenuTabOut,onMousedown:this.handleMenuMousedown,show:this.mergedShow,showCheckmark:this.showCheckmark,resetMenuOnOptionsChange:this.resetMenuOnOptionsChange}),{empty:()=>{var o,i;return[(i=(o=this.$slots).empty)===null||i===void 0?void 0:i.call(o)]},header:()=>{var o,i;return[(i=(o=this.$slots).header)===null||i===void 0?void 0:i.call(o)]},action:()=>{var o,i;return[(i=(o=this.$slots).action)===null||i===void 0?void 0:i.call(o)]}}),this.displayDirective==="show"?[[pt,this.mergedShow],[hn,this.handleMenuClickOutside,void 0,{capture:!0}]]:[[hn,this.handleMenuClickOutside,void 0,{capture:!0}]])):null}})})]}))}});export{Ht as N,Lt as V,ul as _,qt as c,en as m};
