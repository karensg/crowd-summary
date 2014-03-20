<?php echo $this->Html->script('jquery.textHighlighter.min'); ?>
<?php echo $this->Html->script('summary'); ?>
<?php //debug($document); debug($personal_summary); debug($generated_summary);   ?>

<div class="container">
    <div class="summary-container">
        <div class="panel panel-primary">
            <div class="panel-heading"><?php echo $document['Document']['title']; ?></div>
            <div class="panel-body">
                <p>This document is automatically summarized<?php
                    if ($document['Document']['contributions'] > 0) {
                        echo ' and improved by ' . $document['Document']['contributions'] . ' users';
                    }
                    ?>.</p>
                <div class="btn-group">
                    <button type="button" class="btn btn-default active" id="highlight-button">Highlight</button>
                    <button type="button" class="btn btn-default" id="notes-button">Notes</button>
                </div>

                <button type="button" class="btn btn-default right" id="removeAll-button">Remove All Highlights</button>                  
            </div>
        </div>

        <div id="summary">
            <?php
            foreach ($document['Sentence'] as $sentence) {
                echo "<span id='sentence" . $sentence['id'] . "'>" . ($sentence['sentence']) . "</span><br/>";
            }
            ?>
        </div>        
        <?php
        echo $this->Form->create('Summary');
        echo $this->Form->hidden('user_sentences');
        echo $this->Form->submit('Generate', array('class' => 'btn btn-primary right', 'id' => 'generate-button'));
        ?>

        <!-- <button type="submit" class="btn btn-primary right" id="generate-button">Generate</button>  -->

        <div class="clearboth"></div>

        <h1>Summary flavour 1</h1>
        <div id="generated-summary" style="display:none"></div>

        <h1>Summary flavour 2</h1>
        <div id="user-summary"></div>

        <h1>Highlighted sentences dump</h1>
        <div id="ids-dump"></div>

    </div>
</div>

<script type="text/javascript">
    var generated = [];
<?php
foreach ($generated_summary as $sentence) {
    echo "generated.push('" . addslashes($sentence['Summary']['sentence_id']) . "');\n";
}
?>
</script>
