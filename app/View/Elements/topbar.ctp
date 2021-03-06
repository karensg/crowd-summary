<div class="navbar navbar-inverse navbar-fixed-top" role="navigation">
	<div class="container">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
				<span class="sr-only">Toggle navigation</span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="/">WeSummarize</a>
		</div>
		<div class="navbar-collapse collapse">
			<ul class="nav navbar-nav">
				<li id="menu-documents"><?php echo $this->Html->link('All Documents', array('controller' => 'documents', 'action' => 'index')); ?></li>
				<li ><?php echo (isset($user) ? $this->Html->link('My Summaries', array('controller' => 'users', 'action' => 'view', $user)) : ''); ?></li>
				<li id="menu-info"><?php echo $this->Html->link('About', array('controller' => 'info', 'action' => 'index')); ?></li>				
			</ul>
			<?php if (!isset($user)) : ?>
				<?php echo $this->Form->create(null, array('url' => array('controller' => 'users', 'action' => 'login'), 'class' => 'navbar-form navbar-right')); ?>
				<div class="form-group">
					<?php echo $this->Form->input('User.username', array('class' => 'form-control', 'type' => 'text', 'placeholder' => 'username', 'label' => '', 'required' => false)); ?>
				</div>
				<div class="form-group"> 
					<?php echo $this->Form->input('User.password', array('class' => 'form-control', 'placeholder' => 'password', 'label' => '', 'required' => false)); ?>
				</div>
				<div class="form-group">
					<?php echo $this->form->submit('Sign in', array('class' => 'btn btn-success')); ?>
				</div>

				<?php echo $this->Html->link('Sign up', array('controller' => 'users', 'action' => 'add'), array('class' => 'btn btn-primary')); ?>
			<?php else : ?>
				<?php 

				echo $this->Html->link('Sign out', array('controller' => 'users', 'action' => 'logout'), array('class' => 'btn btn-primary navbar-btn', 'id' => 'logout'));
				echo $this->Html->link('<div id="welcomeUser">Logged in as '.$username.'</div>', array('controller' => 'users', 'action' => 'view', $user), array('escape' => false)); ?>
			<?php endif; ?>
		</div>
	</div>
</div>

<?php echo $this->Session->flash('auth'); ?>