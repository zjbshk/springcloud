# springcloud
spring cloud 学习笔记
查看本地状态：`git status`
将工作区的更改提交到暂存区：`git add (./file/dir)`
将暂存区的更改提交到当前分支：`git commit -m 备注信息`
将本地分支上传到远端：`git push`

查看所有配置：`git config --list`
设置当前输入远端的流到那个分支：`git push --set-upstream 远程仓库名 分支名`
删除文件：`git rm <file>`
克隆远程仓库：`git clone <url>`

### 分支管理
创建并切换分支：`git checkout -b 分支名`
通过远程分支克隆创建分支：`git checkout -b 本地分支名  远程仓库名/远程分支名`
查看所有分支：`git branch`
切换分支：`git checkout 分支名`
删除分支：`git branch -d 分支名`
合并分支（可能没有历史合并记录）：`git merge 分支名`
创建分支：`git branch <name>`
查看分支合并：`git log --graph`
更方便的查看分支合并：`git log --graph --pretty=oneline --abbrev-commit`
合并分支（有历史合并记录）：`git merge --no-ff -m "merge with no-ff" 分支名`
强行删除未被合并的分支：`git branch -D 分支名`
推送分支：`git push 远程仓库名  本地分支名`
拉取远程分支：`git pull  <remote> <branch>`
创建本地分支和远程分支的链接关系：`git branch --set-upstream-to <branch-name> origin/<branch-name>`
推送分支：`git push 远程仓库名  head:远程分支`

### Bug管理
生成一个储存区：`git stash`
显示储存区列表：`git stash list`
恢复储存区：`git stash apply 储存区名字`
删除储存区：`git stash drop  储存区名字`
恢复并删除最近的储存区：`git stash pop`

查看远程信息：`git remote`
查看详细远程信息：`git remote -v`
设置远程仓库信息：`git remote add 远程仓库名 URL`


### 修改撤销
并没有提交到暂存区：
`git checkout -- <file>`
提交暂存区，但是并没有commit到本地分支：
`git reset head <file>`
`git checkout -- <file>`
commit到了本地分支：
`git reset head~1 `

#### 标签管理
查看全部tag：`git tag`
标记tag：`git tag <tag>`
在指定的commit上标记tag：`git tag <tag> <commit id>`
显示tag信息：`git show <tag>`
删除本地tag：`git tag -d <tag>`
将标记tag推送到远程：`git push <远程名> <tag>`
推送全部未推送过的本地标签：`git push origin --tags`
删除一个远程标签：`git push 远程名 :refs/tags/<tagname>`

1.git add .
git reset --hard
2.git stash
git stash drop

